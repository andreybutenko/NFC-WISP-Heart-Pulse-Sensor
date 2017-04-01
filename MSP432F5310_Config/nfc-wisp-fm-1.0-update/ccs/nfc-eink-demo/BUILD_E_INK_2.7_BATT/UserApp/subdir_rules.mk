################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Each subdirectory must supply rules for building sources it contributes
UserApp/myApp.obj: ../UserApp/myApp.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_7 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="UserApp/myApp.d" --obj_directory="UserApp" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

UserApp/myE-paperApp.obj: ../UserApp/myE-paperApp.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_7 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="UserApp/myE-paperApp.d" --obj_directory="UserApp" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

UserApp/myNFC_Protocol.obj: ../UserApp/myNFC_Protocol.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_7 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="UserApp/myNFC_Protocol.d" --obj_directory="UserApp" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '

UserApp/tempSense.obj: ../UserApp/tempSense.c $(GEN_OPTS) $(GEN_HDRS)
	@echo 'Building file: $<'
	@echo 'Invoking: MSP430 Compiler'
	"/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/bin/cl430" -vmspx --abi=eabi --code_model=small --data_model=small -O1 --include_path="/Applications/ti/ccsv6/ccs_base/msp430/include" --include_path="/Applications/ti/ccsv6/tools/compiler/ti-cgt-msp430_15.12.1.LTS/include" --advice:power_severity=remark --advice:power="all" -g --define=__MSP430F5310__ --define=E_INK_2_7 --diag_warning=225 --display_error_number --silicon_errata=CPU21 --silicon_errata=CPU22 --silicon_errata=CPU23 --silicon_errata=CPU40 --printf_support=minimal --preproc_with_compile --preproc_dependency="UserApp/tempSense.d" --obj_directory="UserApp" $(GEN_OPTS__FLAG) "$<"
	@echo 'Finished building: $<'
	@echo ' '


