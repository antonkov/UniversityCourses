extern puts, putchar, malloc, printf

section .text
global IDCT

IDCT:
  ; prologue
    push ebp
    push ebx
    push esi
    push edi

    mov ebx, [esp + 28]
    mov [count_tests], ebx

    mov ecx, [count_tests]
    
    mov eax, [esp + 20]
    mov ebx, [esp + 24]

     pusha
     rdtsc    
     mov [start], eax
     popa

  
main_loop:
   push eax
    push ebx
    push ecx
    
    push eax
    push middle_state
    call make_1d_dct_row
    add esp, 8
;        pusha
;    fld dword [middle_state + 4]
;    sub esp, 8
;    fstp qword [esp]
;    push hello
;    call printf
;    add esp, 12
;    popa

    mov ebx, [esp + 4]
   push middle_state
    push ebx
    call make_1d_dct_col
    add esp, 8

    pop ecx
    pop ebx
    pop eax

    add eax, [size_of_test]
    add ebx, [size_of_test]
    dec ecx
    jnz main_loop
    
        pusha
     rdtsc    
     mov ebx, [start]
     sub ebx, eax
     push ebx
     push time
    call printf
    add esp, 8
    popa
;epilogue
    pop edi
    pop esi
    pop ebx
    pop ebp

  ret

make_1d_dct_row:
    mov eax, [esp + 8] ; eax - current y
    mov ebx, [esp + 4]
    mov cl, 8
fori2:
    xorps xmm0, xmm0
    xorps xmm1, xmm1
    mov edx, cos_row
    mov ch, 8
fork2:
    movss xmm2, [eax]
    pshufd xmm2, xmm2, 0

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add eax, 4
    add edx, 32
    dec ch
    jnz fork2

    movaps [ebx], xmm0
    movaps [ebx + 16], xmm1
    add ebx, 32
    dec cl
    jnz fori2
    ret
;##############################################3

make_1d_dct_col:
    mov eax, [esp + 8] ; eax - input 
    mov ebx, [esp + 4] ; ebx - output

    mov esi, cos_col ; esi - current cos
    mov cl, 8 ; cl - j
fori:
    xorps xmm0, xmm0
    xorps xmm1, xmm1
    mov edx, eax ; edx - current row
    mov ch, 8 ; ch - k
fork:
    movss xmm2, [esi]
    pshufd xmm2, xmm2, 0
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

   add esi, 4
    add edx, 32
    dec ch
    jnz fork

    movaps [ebx], xmm0
    movaps [ebx + 16], xmm1
    add ebx, 32
    dec cl
    jnz fori
    ret

section .bss
align 16
middle_state resd 64

section .data

hello db "Hello %.3f", 0xa, 0
time  db "time %d", 0xa, 0
size_one     dd 8
size_of_test dd 256
count_tests  dd 0
start dd 0
example dd 0.2
align 16
cos_col dd 0.125, 0.24519631, 0.23096988, 0.2078674, 0.17677669, 0.13889256, 0.09567086, 0.04877258
    dd 0.125, 0.2078674, 0.09567086, -0.04877258, -0.17677669, -0.24519631, -0.23096988, -0.13889256
    dd 0.125, 0.13889256, -0.09567086, -0.24519631, -0.17677669, 0.04877258, 0.23096988, 0.2078674
    dd 0.125, 0.04877258, -0.23096988, -0.13889256, 0.17677669, 0.2078674, -0.09567086, -0.24519631
    dd 0.125, -0.04877258, -0.23096988, 0.13889256, 0.17677669, -0.2078674, -0.09567086, 0.24519631
    dd 0.125, -0.13889256, -0.09567086, 0.24519631, -0.17677669, -0.04877258, 0.23096988, -0.2078674
    dd 0.125, -0.2078674, 0.09567086, 0.04877258, -0.17677669, 0.24519631, -0.23096988, 0.13889256
    dd 0.125, -0.24519631, 0.23096988, -0.2078674, 0.17677669, -0.13889256, 0.09567086, -0.04877258

cos_row dd 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125
    dd 0.24519631, 0.2078674, 0.13889256, 0.04877258, -0.04877258, -0.13889256, -0.2078674, -0.24519631
    dd 0.23096988, 0.09567086, -0.09567086, -0.23096988, -0.23096988, -0.09567086, 0.09567086, 0.23096988
    dd 0.2078674, -0.04877258, -0.24519631, -0.13889256, 0.13889256, 0.24519631, 0.04877258, -0.2078674
    dd 0.17677669, -0.17677669, -0.17677669, 0.17677669, 0.17677669, -0.17677669, -0.17677669, 0.17677669
    dd 0.13889256, -0.24519631, 0.04877258, 0.2078674, -0.2078674, -0.04877258, 0.24519631, -0.13889256
    dd 0.09567086, -0.23096988, 0.23096988, -0.09567086, -0.09567086, 0.23096988, -0.23096988, 0.09567086
    dd 0.04877258, -0.13889256, 0.2078674, -0.24519631, 0.24519631, -0.2078674, 0.13889256, -0.04877258

