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

.data   
prompt1:        .asciiz "Enter the first integer: "
prompt2:        .asciiz "Enter the second integer: "
label_large:    .asciiz "Larger integer: "
label_small:    .asciiz "Smaller integer: "
label_quot:     .asciiz "Quotient: "
label_rem:      .asciiz "Remainder: "
newline:        .asciiz "\n"
error_div0:     .asciiz "Error: Division by zero is undefined.\n"
data_mem:       .word   0, 0, 0, 0

.text   
                .globl  main
main:           
    ## print prompt1: "Enter the first integer: "
    addi    $v0,            $zero,          4                       # load print string syscall
    lui     $a0,            0x1001                                  # load upper half of prompt1 address
    ori     $a0,            $a0,            0x0000                  # load lower half of prompt1 address
    syscall                                                         # execute syscall to print prompt1
    ## read the first integer
    addi    $v0,            $zero,          5                       # load read integer syscall
    syscall                                                         # execute syscall to read first integer
    add     $t0,            $v0,            $zero                   # store the first integer in $t0

    ## print prompt2: "Enter the second integer: "
    addi    $v0,            $zero,          4                       # load print string syscall
    lui     $a0,            0x1001                                  # load upper half of prompt2 address
    ori     $a0,            $a0,            0x001A                  # load lower half of prompt2 address
    syscall                                                         # execute syscall to print prompt2
    ## read the second integer
    addi    $v0,            $zero,          5                       # load read integer syscall
    syscall                                                         # execute syscall to read second integer
    add     $t1,            $v0,            $zero                   # store the second integer in $t1

    ## compare the integers to find the larger one
    slt     $t2,            $t0,            $t1                     # set $t2 = 1 if $t0 < $t1, else 0
    beq     $t2,            $zero,          store_large_first       # if $t0 >= $t1, go to store_large_first
    ## $t0 is smaller, store $t0 in data_mem and $t1 in data_mem+4
    sw      $t0,            data_mem                                # store $t0 (smaller) in data_mem
    sw      $t1,            data_mem+4                              # store $t1 (larger) in data_mem+4
    j       check_division                                          # jump to check_division

store_large_first:
    ## $t1 is smaller, store $t1 in data_mem and $t0 in data_mem+4
    sw      $t1,            data_mem                                # store $t1 (smaller) in data_mem
    sw      $t0,            data_mem+4                              # store $t0 (larger) in data_mem+4

check_division: 
    ## load integers and check for division by zero
    lw      $t3,            data_mem+4                              # load larger integer into $t3
    lw      $t4,            data_mem                                # load smaller integer into $t4
    beq     $t4,            $zero,          handle_div0             # if $t4 == 0, go to handle_div0

    ## perform the division
    div     $t3,            $t4                                     # divide larger by smaller
    mflo    $t5                                                     # move quotient to $t5
    mfhi    $t6                                                     # move remainder to $t6
    sw      $t5,            data_mem+8                              # store quotient in data_mem+8
    sw      $t6,            data_mem+12                             # store remainder in data_mem+12
    j       print_results                                           # jump to print_results

handle_div0:    
    ## division by zero: set quotient and remainder to 0
    sw      $zero,          data_mem+8                              # set quotient to 0
    sw      $zero,          data_mem+12                             # set remainder to 0
    addi    $v0,            $zero,          4                       # load print string syscall
    lui     $a0,            0x1001                                  # load upper half of error_div0 address
    ori     $a0,            $a0,            0x0071                  # load lower half of error_div0 address
    syscall                                                         # print "Error: Division by zero"

print_results:  
    ## print "Larger integer: "
    addi    $v0,            $zero,          4                       # load print string syscall
    lui     $a0,            0x1001                                  # load upper half of label_large address
    ori     $a0,            $a0,            0x0035                  # load lower half of label_large address
    syscall                                                         # execute syscall to print label_large
    ## print the larger integer
    lw      $a0,            data_mem+4                              # load larger integer from memory
    addi    $v0,            $zero,          1                       # load print integer syscall
    syscall                                                         # execute syscall to print the larger integer
    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x006F
    syscall 

    ## print "Smaller integer: "
    addi    $v0,            $zero,          4                       # load print string syscall
    lui     $a0,            0x1001                                  # load upper half of label_small address
    ori     $a0,            $a0,            0x0046                  # load lower half of label_small address
    syscall                                                         # execute syscall to print label_small
    ## print the smaller integer
    lw      $a0,            data_mem                                # load smaller integer from memory
    addi    $v0,            $zero,          1                       # load print integer syscall
    syscall                                                         # execute syscall to print the smaller integer
    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x006F
    syscall 

    beq     $t4,            $zero,          exit_program

    addi    $v0,            $zero,          4                       # load print string syscall
    lui     $a0,            0x1001                                  # load upper half of label_quot address
    ori     $a0,            $a0,            0x0058                  # load lower half of label_quot address
    syscall                                                         # execute syscall to print label_quot
    lw      $a0,            data_mem+8                              # load quotient from memory
    addi    $v0,            $zero,          1                       # load print integer syscall
    syscall                                                         # execute syscall to print quotient
    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x006F
    syscall 

    addi    $v0,            $zero,          4                       # load print string syscall
    lui     $a0,            0x1001                                  # load upper half of label_rem address
    ori     $a0,            $a0,            0x0063                  # load lower half of label_rem address
    syscall                                                         # execute syscall to print label_rem
    lw      $a0,            data_mem+12                             # load remainder from memory
    addi    $v0,            $zero,          1                       # load print integer syscall
    syscall                                                         # execute syscall to print remainder
    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x006F
    syscall 

exit_program:   
    addi    $v0,            $zero,          10
    syscall 
