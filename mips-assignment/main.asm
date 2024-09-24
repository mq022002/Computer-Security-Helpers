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
    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x0000
    syscall 
    addi    $v0,            $zero,          5
    syscall 
    add     $t0,            $v0,            $zero

    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x001A
    syscall 
    addi    $v0,            $zero,          5
    syscall 
    add     $t1,            $v0,            $zero

    slt     $t2,            $t0,            $t1
    beq     $t2,            $zero,          store_large_first
    sw      $t0,            data_mem
    sw      $t1,            data_mem+4
    j       check_division

store_large_first:
    sw      $t1,            data_mem
    sw      $t0,            data_mem+4

check_division: 
    lw      $t3,            data_mem+4
    lw      $t4,            data_mem
    beq     $t4,            $zero,          handle_div0

    div     $t3,            $t4
    mflo    $t5
    mfhi    $t6
    sw      $t5,            data_mem+8
    sw      $t6,            data_mem+12
    j       print_results

handle_div0:    
    sw      $zero,          data_mem+8
    sw      $zero,          data_mem+12
    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x0071
    syscall 

print_results:  
    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x0035
    syscall 
    lw      $a0,            data_mem+4
    li      $v0,            1
    syscall 
    li      $v0,            4
    la      $a0,            newline
    syscall 

    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x0046
    syscall 
    lw      $a0,            data_mem
    li      $v0,            1
    syscall 
    li      $v0,            4
    la      $a0,            newline
    syscall 

    beq     $t4,            $zero,          exit_program

    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x0058
    syscall 
    lw      $a0,            data_mem+8
    li      $v0,            1
    syscall 
    li      $v0,            4
    la      $a0,            newline
    syscall 

    addi    $v0,            $zero,          4
    lui     $a0,            0x1001
    ori     $a0,            $a0,            0x0063
    syscall 
    lw      $a0,            data_mem+12
    li      $v0,            1
    syscall 
    li      $v0,            4
    la      $a0,            newline
    syscall 

exit_program:   
    li      $v0,            10
    syscall 
