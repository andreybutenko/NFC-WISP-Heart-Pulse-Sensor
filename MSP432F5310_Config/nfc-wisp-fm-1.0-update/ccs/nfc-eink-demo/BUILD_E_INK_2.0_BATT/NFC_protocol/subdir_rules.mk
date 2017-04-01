################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Each subdirectory must supply rules for building sources it contributes
NFC_protocol/RX_ISR.obj: ../NFC_protocol/RX_ISR.asm $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_0 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="NFC_protocol/RX_ISR.d" --obj_directory="NFC_protocol" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

NFC_protocol/_14443_B.obj: ../NFC_protocol/_14443_B.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_0 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="NFC_protocol/_14443_B.d" --obj_directory="NFC_protocol" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

NFC_protocol/_14443_B_protocol.obj: ../NFC_protocol/_14443_B_protocol.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_0 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="NFC_protocol/_14443_B_protocol.d" --obj_directory="NFC_protocol" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

NFC_protocol/crc_checker.obj: ../NFC_protocol/crc_checker.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_0 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="NFC_protocol/crc_checker.d" --obj_directory="NFC_protocol" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

NFC_protocol/doNFC.obj: ../NFC_protocol/doNFC.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_0 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="NFC_protocol/doNFC.d" --obj_directory="NFC_protocol" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

NFC_protocol/send_bpsk.obj: ../NFC_protocol/send_bpsk.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_0 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="NFC_protocol/send_bpsk.d" --obj_directory="NFC_protocol" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '


