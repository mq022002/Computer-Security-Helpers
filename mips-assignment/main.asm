    ## CS 385 MIPS Assignment
    ##
    ## Programmer: Matthew Quijano
    ## Date: 24 September 2024
    ##
    ## Register Use:
    ##
    ## $v0       system call code
    ## $a0       argument register for syscalls (print strings or display results)
    ## $t0       first integer input
    ## $t1       second integer input
    ## $t2       temporary, comparison result
    ## $t3       larger integer
    ## $t4       smaller integer
    ## $t5       quotient
    ## $t6       remainder

.text   
                .globl  main
main:           
    ## print prompt1: "Enter the first integer: "
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of prompt1 address into $a0
    ori     $a0,            $a0,            0x0000                  # I-type, Immediate format, load lower half of prompt1 address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print prompt1
    ## read the first integer
    addi    $v0,            $zero,          5                       # I-type, Immediate format, load read integer syscall into $v0
    syscall                                                         # R-type, Special format, execute syscall to read first integer
    add     $t0,            $v0,            $zero                   # R-type, Register format, store the first integer in $t0

    ## print prompt2: "Enter the second integer: "
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of prompt2 address into $a0
    ori     $a0,            $a0,            0x001A                  # I-type, Immediate format, load lower half of prompt2 address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print prompt2
    ## read the second integer
    addi    $v0,            $zero,          5                       # I-type, Immediate format, load read integer syscall into $v0
    syscall                                                         # R-type, Special format, execute syscall to read second integer
    add     $t1,            $v0,            $zero                   # R-type, Register format, store the second integer in $t1

    ## compare the integers to find the larger one
    slt     $t2,            $t0,            $t1                     # R-type, Register format, set $t2 = 1 if $t0 < $t1, else 0
    beq     $t2,            $zero,          store_large_first       # I-type, Immediate format, if $t0 >= $t1, go to store_large_first
    ## $t0 is smaller, store $t0 in data_mem and $t1 in data_mem+4
    sw      $t0,            data_mem                                # I-type, Immediate format, store $t0 (smaller) in data_mem
    sw      $t1,            data_mem+4                              # I-type, Immediate format, store $t1 (larger) in data_mem+4
    j       check_division                                          # J-type, Jump format, jump to check_division

store_large_first:
    ## $t1 is smaller, store $t1 in data_mem and $t0 in data_mem+4
    sw      $t1,            data_mem                                # I-type, Immediate format, store $t1 (smaller) in data_mem
    sw      $t0,            data_mem+4                              # I-type, Immediate format, store $t0 (larger) in data_mem+4

check_division: 
    ## load integers and check for division by zero
    lw      $t3,            data_mem+4                              # I-type, Immediate format, load larger integer into $t3
    lw      $t4,            data_mem                                # I-type, Immediate format, load smaller integer into $t4
    beq     $t4,            $zero,          handle_div0             # I-type, Immediate format, if $t4 == 0, go to handle_div0

    ## perform the division
    div     $t3,            $t4                                     # R-type, Register format, divide larger by smaller
    mflo    $t5                                                     # R-type, Special format, move quotient to $t5
    mfhi    $t6                                                     # R-type, Special format, move remainder to $t6
    sw      $t5,            data_mem+8                              # I-type, Immediate format, store quotient in data_mem+8
    sw      $t6,            data_mem+12                             # I-type, Immediate format, store remainder in data_mem+12
    j       print_results                                           # J-type, Jump format, jump to print_results

handle_div0:    
    ## division by zero: set quotient and remainder to 0
    sw      $zero,          data_mem+8                              # I-type, Immediate format, store 0 (quotient) in data_mem+8
    sw      $zero,          data_mem+12                             # I-type, Immediate format, store 0 (remainder) in data_mem+12
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of error_div0 address into $a0
    ori     $a0,            $a0,            0x0071                  # I-type, Immediate format, load lower half of error_div0 address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print "Error: Division by zero"

print_results:  
    ## print "Larger integer: "
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of label_large address into $a0
    ori     $a0,            $a0,            0x0035                  # I-type, Immediate format, load lower half of label_large address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print label_large
    ## print the larger integer
    lw      $a0,            data_mem+4                              # I-type, Immediate format, load larger integer from memory into $a0
    addi    $v0,            $zero,          1                       # I-type, Immediate format, load print integer syscall into $v0
    syscall                                                         # R-type, Special format, execute syscall to print the larger integer
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of the address into $a0
    ori     $a0,            $a0,            0x006F                  # I-type, Immediate format, load lower half of the address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print the string

    ## print "Smaller integer: "
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of label_small address into $a0
    ori     $a0,            $a0,            0x0046                  # I-type, Immediate format, load lower half of label_small address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print label_small
    ## print the smaller integer
    lw      $a0,            data_mem                                # I-type, Immediate format, load smaller integer from memory into $a0
    addi    $v0,            $zero,          1                       # I-type, Immediate format, load print integer syscall into $v0
    syscall                                                         # R-type, Special format, execute syscall to print the smaller integer
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of the address into $a0
    ori     $a0,            $a0,            0x006F                  # I-type, Immediate format, load lower half of the address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print the string

    ## exit program if division by zero
    beq     $t4,            $zero,          exit_program            # I-type, Immediate format, if $t4 == 0, go to exit_program

    ## print "Quotient: "
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of label_quot address into $a0
    ori     $a0,            $a0,            0x0058                  # I-type, Immediate format, load lower half of label_quot address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print label_quot
    ## print the quotient
    lw      $a0,            data_mem+8                              # I-type, Immediate format, load quotient from memory into $a0
    addi    $v0,            $zero,          1                       # I-type, Immediate format, load print integer syscall into $v0
    syscall                                                         # R-type, Special format, execute syscall to print quotient
    ## print a string
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of the address into $a0
    ori     $a0,            $a0,            0x006F                  # I-type, Immediate format, load lower half of the address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print the string

    ## print "Remainder: "
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of label_rem address into $a0
    ori     $a0,            $a0,            0x0063                  # I-type, Immediate format, load lower half of label_rem address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print label_rem
    ## print the remainder
    lw      $a0,            data_mem+12                             # I-type, Immediate format, load remainder from memory into $a0
    addi    $v0,            $zero,          1                       # I-type, Immediate format, load print integer syscall into $v0
    syscall                                                         # R-type, Special format, execute syscall to print remainder
    ## print a string
    addi    $v0,            $zero,          4                       # I-type, Immediate format, load print string syscall into $v0
    lui     $a0,            0x1001                                  # I-type, Immediate format, load upper half of the address into $a0
    ori     $a0,            $a0,            0x006F                  # I-type, Immediate format, load lower half of the address into $a0
    syscall                                                         # R-type, Special format, execute syscall to print the string

exit_program:   
    ## exit the program
    addi    $v0,            $zero,          10                      # I-type, Immediate format, load exit syscall into $v0
    syscall                                                         # R-type, Special format, execute syscall to exit the program

.data                                                               # Start of the data segment
prompt1:        .asciiz "Enter the first integer: "                 # Null-terminated string for the first prompt
prompt2:        .asciiz "Enter the second integer: "                # Null-terminated string for the second prompt
label_large:    .asciiz "Larger integer: "                          # Null-terminated string for the larger integer label
label_small:    .asciiz "Smaller integer: "                         # Null-terminated string for the smaller integer label
label_quot:     .asciiz "Quotient: "                                # Null-terminated string for the quotient label
label_rem:      .asciiz "Remainder: "                               # Null-terminated string for the remainder label
newline:        .asciiz "\n"                                        # Null-terminated string for a newline character
error_div0:     .asciiz "Error: Division by zero is undefined.\n"   # Null-terminated string for the division by zero error message
data_mem:       .word   0, 0, 0, 0                                  # Array of four words (32-bit integers), initialized to 0