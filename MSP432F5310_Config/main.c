#include <msp430f5310.h>

unsigned char RXData;
unsigned char TXByteCtr;
unsigned char RXByteCtr;
unsigned char TXData;
unsigned char TXAddr;
unsigned char MODE;
unsigned char *PRxData;                     // Pointer to RX data

void sys_init(void) {
    WDTCTL = WDTPW | WDTHOLD; // Stop watchdog timer

    P2DIR |= 0x02;
    P2SEL |= 0x02;

    P5SEL |= 0x0C;
    UCSCTL6 &= ~XT2OFF;
    UCSCTL3 |= SELREF_2;                      // FLLref = REFO
											 // Since LFXT1 is not used,
											 // sourcing FLL with LFXT1 can cause
											 // XT1OFFG flag to set
    UCSCTL4 |= SELA_2;                        // ACLK=REFO,SMCLK=DCO,MCLK=DCO

    // Loop until XT1,XT2 & DCO stabilizes - in this case loop until XT2 settles
	do
	{
	UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
											// Clear XT2,XT1,DCO fault flags
	SFRIFG1 &= ~OFIFG;                      // Clear fault flags
	}while (SFRIFG1&OFIFG);                   // Test oscillator fault flag

	UCSCTL6 &= ~XT2DRIVE0;                    // Decrease XT2 Drive according to
	                                          // expected frequency
	UCSCTL4 |= SELS_5 + SELM_5;               // SMCLK=MCLK=XT2
}

void I2C_config(void) {
    P4SEL |= 0x06; // Assign P4.1 P4.2 to secondary digital function (ISC_CLK, ISC_SDA)

    UCB1CTL1 |= UCSWRST;    // Enable SW reset
    UCB1CTL0 = 0x00;
    UCB1CTL0 = UCMST + UCMODE_3 + UCSYNC;

    // TODO: Configure Clock Setting
    UCB1CTL1 = UCSSEL_2 + UCSWRST; // UCSSEL_2 Clock source 2 (SMCLK)



    UCB1BR0 = 24; // fSCL = SMCLK/12 = ~100kHz (Need to verify [SMCLK = 13.56MHz])
    UCB1BR1 = 0;
    UCB1CTL1 &= ~UCSWRST;   // Clear SW reset, resume operation


}

void I2C_Tx_config(unsigned char slave_addr) {
	UCB1CTL1 |= UCSWRST;
	//UCB1CTL1 |= UCTR; // I2C transmitter mode
	UCB1I2CSA = slave_addr;  // TODO: Find correct slave register address
	UCB1CTL1 &= ~UCSWRST;   // Clear SW reset, resume operation
	UCB1IE |= UCTXIE + UCNACKIE + UCRXIE;   // Enable TX interrupt
}



#pragma vector = USCI_B1_VECTOR
__interrupt void USCI_B1_ISR(void) {
    switch(__even_in_range(UCB1IV, 12)) {
      case  0: break;                           // Vector  0: No interrupts
      case  2: break;                           // Vector  2: ALIFG
      case  4: break;                           // Vector  4: NACKIFG
      case  6: break;                           // Vector  6: STTIFG
      case  8: break;                           // Vector  8: STPIFG
      case 10:                                  // Vector 10: RXIFG
    	  	  RXByteCtr--;                            // Decrement RX byte counter
    	      if (RXByteCtr)
    	      {
    	        *PRxData++ = UCB1RXBUF;               // Move RX data to address PRxData
    	        if (RXByteCtr == 1)                   // Only one byte left?
    	          UCB1CTL1 |= UCTXSTP;                // Generate I2C stop condition
    	      }
    	      else
    	      {
    	        *PRxData = UCB1RXBUF;                 // Move final RX data to PRxData
    	        __bic_SR_register_on_exit(LPM0_bits); // Exit active CPU
    	      }
        break;
      case 12:

    	  	  if (TXByteCtr > 1)                          // Check TX byte counter
    	      {
    	  		  UCB1TXBUF = TXAddr;                   // Load TX buffer
    	  		  TXByteCtr--;                          // Decrement TX byte counter
    	      }
    	  	  else if (TXByteCtr > 0) {
    	  		  UCB1TXBUF = TXData;
    	  		  TXByteCtr--;
    	  	  }
    	      else
    	      {

    	        UCB1CTL1 |= UCTXSTP;                  // I2C stop condition
    	        UCB1IFG &= ~UCTXIFG;                  // Clear USCI_B0 TX int flag
    	        if(MODE) {
    	        		UCB1CTL1 &= ~UCTR;
    	        }
    	        __bic_SR_register_on_exit(LPM0_bits); // Exit LPM0
    	      }
    	  	//UCB1TXBUF = TXData;
    	  	//UCB1CTL1 |= UCTXSTP;                  // I2C stop condition
		//UCB1IFG &= ~UCTXIFG;                  // Clear USCI_B0 TX int flag
		//__bic_SR_register_on_exit(LPM0_bits); // Exit LPM0
    	    	break;                           // Vector 12: TXIFG
      default: break;
    }
}



void DCO_setup(void) {
	WDTCTL = WDTPW | WDTHOLD; // Stop watchdog timer
	UCSCTL3 = SELREF_2;                       // Set DCO FLL reference = REFO
	UCSCTL4 |= SELA_3;                        // Set ACLK = REFO
	UCSCTL0 = 0x0000;                         // Set lowest possible DCOx, MODx

	// Loop until XT1,XT2 & DCO stabilizes - In this case only DCO has to stabilize
	do
	{
	UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
											// Clear XT2,XT1,DCO fault flags
	SFRIFG1 &= ~OFIFG;                      // Clear fault flags
	}while (SFRIFG1&OFIFG);                   // Test oscillator fault flag

	__bis_SR_register(SCG0);                  // Disable the FLL control loop
	UCSCTL1 = DCORSEL_5;                      // Select DCO range 16MHz operation
	UCSCTL2 |= 249;                           // Set DCO Multiplier for 8MHz
											// (N + 1) * FLLRef = Fdco
											// (249 + 1) * 32768 = 8MHz
	__bic_SR_register(SCG0);                  // Enable the FLL control loop

	// Worst-case settling time for the DCO when the DCO range bits have been
	// changed is n x 32 x 32 x f_MCLK / f_FLL_reference. See UCS chapter in 5xx
	// UG for optimization.
	// 32 x 32 x 8 MHz / 32,768 Hz = 250000 = MCLK cycles for DCO to settle
	__delay_cycles(250000);
}

void I2C_Tx(unsigned char tx_addr, unsigned char tx_data) {
	MODE = 0x00;
	TXAddr = tx_addr;
	TXData = tx_data;
	TXByteCtr = 0x02;
	while (UCB1CTL1 & UCTXSTP);             // Ensure stop condition got sent
	UCB1CTL1 |= UCTR + UCTXSTT;             // I2C TX, start condition

	__bis_SR_register(LPM0_bits + GIE);     // Enter LPM0 w/ interrupts
	__no_operation();
	__delay_cycles(1000);
}

void I2C_Rx(unsigned char rx_addr) {
	RXByteCtr = 0x04;
	MODE = 0x01;
	TXData = rx_addr;
	TXByteCtr = 0x01;
	while (UCB1CTL1 & UCTXSTP);             // Ensure stop condition got sent
	UCB1CTL1 |= UCTR + UCTXSTT;             // I2C TX, start condition

	__bis_SR_register(LPM0_bits + GIE);     // Enter LPM0 w/ interrupts
	__no_operation();



	while (UCB1CTL1 & UCTXSTP);             // Ensure stop condition got sent
	UCB1CTL1 |= UCTXSTT;                    // I2C start condition


	__bis_SR_register(LPM0_bits + GIE);     // Enter LPM0, enable interrupts
	__no_operation();
}

void main(void) {
    sys_init();

    /* -- DCO test -- */
	//DCO_setup();
	//P4SEL |= BIT2;
	//P4DIR |= BIT2;
    //P4OUT &= ~BIT2;

    I2C_config();
    I2C_Tx_config(0x57);

    I2C_Tx(0x06, 0x03);
    I2C_Tx(0x07, 0x43);
    I2C_Tx(0x09, 0x33);

	while(1) {

		I2C_Rx(0x05);


	}
}
