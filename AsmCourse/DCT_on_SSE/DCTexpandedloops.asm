extern puts, putchar, malloc, printf

section .text
global DCT

DCT:
;prologue
    push ebp
    push ebx
    push esi
    push edi

    mov edi, [esp + 28]
    mov [count_tests], edi

    mov ebx, [count_tests]
    
    mov esi, [esp + 20]
    mov edi, [esp + 24]

     pusha
     rdtsc    
     mov [start], eax
     popa

  
main_loop:

 make_1d_dct_row:
    mov eax, middle_state
    mov cl, 8
fori2:
    xorps xmm0, xmm0
    xorps xmm1, xmm1
    mov edx, cos_row
;######################START
    movaps xmm6, [esi]
    movaps xmm7, [esi + 16]
    pshufd xmm2, xmm6, 0

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add edx, 32

    pshufd xmm2, xmm6, 85

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add edx, 32

    pshufd xmm2, xmm6, 170

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add edx, 32

    pshufd xmm2, xmm6, 255

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add edx, 32

    pshufd xmm2, xmm7, 0

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add edx, 32

    pshufd xmm2, xmm7, 85

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add edx, 32

    pshufd xmm2, xmm7, 170

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    add edx, 32

    pshufd xmm2, xmm7, 255

   movaps xmm3, [edx]
    movaps xmm4, [edx + 16]
    
    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4
    
    movaps [eax], xmm0
    movaps [eax + 16], xmm1
    add esi, 32
    add eax, 32
    dec cl
    jnz fori2


make_1d_dct_col:
    mov eax, cos_col ; eax - current cos
    mov cl, 8 ; cl - j
fori:
    xorps xmm0, xmm0
    xorps xmm1, xmm1
    mov edx, middle_state ; edx - current row
;#################START###############
    movaps xmm6, [eax]
    movaps xmm7, [eax + 16]
    pshufd xmm2, xmm6, 0
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add edx, 32

    pshufd xmm2, xmm6, 85
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add edx, 32

    pshufd xmm2, xmm6, 170
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add edx, 32

    pshufd xmm2, xmm6, 255
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add edx, 32

    pshufd xmm2, xmm7, 0
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add edx, 32

    pshufd xmm2, xmm7, 85
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add edx, 32

    pshufd xmm2, xmm7, 170
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add edx, 32

    pshufd xmm2, xmm7, 255
   
    movaps xmm3, [edx]
    movaps xmm4, [edx + 16]

    mulps xmm3, xmm2
    mulps xmm4, xmm2
    
    addps xmm0, xmm3
    addps xmm1, xmm4

    add eax, 32
    movaps [edi], xmm0
    movaps [edi + 16], xmm1
    add edi, 32
    dec cl
    jnz fori

    dec ebx
    jnz main_loop
    
      pusha
     rdtsc    
     mov edi, [start]
     sub eax, edi
     push eax
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

;##############################################3

section .bss
align 16
middle_state resd 64

section .data

hello db "Hello %.3f", 0xa, 0
time  db "time %d", 0xa, 0
size_one     dd 8
size_of_test dd 256
count_tests  dd 0
example dd 0.2
start dd 0
finish dd 0
align 16
cos_row dd 1.0, 0.98078525, 0.9238795, 0.8314696, 0.70710677, 0.55557024, 0.38268343, 0.19509032
    dd 1.0, 0.8314696, 0.38268343, -0.19509032, -0.70710677, -0.98078525, -0.9238795, -0.55557024
    dd 1.0, 0.55557024, -0.38268343, -0.98078525, -0.70710677, 0.19509032, 0.9238795, 0.8314696
    dd 1.0, 0.19509032, -0.9238795, -0.55557024, 0.70710677, 0.8314696, -0.38268343, -0.98078525
    dd 1.0, -0.19509032, -0.9238795, 0.55557024, 0.70710677, -0.8314696, -0.38268343, 0.98078525
    dd 1.0, -0.55557024, -0.38268343, 0.98078525, -0.70710677, -0.19509032, 0.9238795, -0.8314696
    dd 1.0, -0.8314696, 0.38268343, 0.19509032, -0.70710677, 0.98078525, -0.9238795, 0.55557024
    dd 1.0, -0.98078525, 0.9238795, -0.8314696, 0.70710677, -0.55557024, 0.38268343, -0.19509032

cos_col dd 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0
    dd 0.98078525, 0.8314696, 0.55557024, 0.19509032, -0.19509032, -0.55557024, -0.8314696, -0.98078525
    dd 0.9238795, 0.38268343, -0.38268343, -0.9238795, -0.9238795, -0.38268343, 0.38268343, 0.9238795
    dd 0.8314696, -0.19509032, -0.98078525, -0.55557024, 0.55557024, 0.98078525, 0.19509032, -0.8314696
    dd 0.70710677, -0.70710677, -0.70710677, 0.70710677, 0.70710677, -0.70710677, -0.70710677, 0.70710677
    dd 0.55557024, -0.98078525, 0.19509032, 0.8314696, -0.8314696, -0.19509032, 0.98078525, -0.55557024
    dd 0.38268343, -0.9238795, 0.9238795, -0.38268343, -0.38268343, 0.9238795, -0.9238795, 0.38268343
    dd 0.19509032, -0.55557024, 0.8314696, -0.98078525, 0.98078525, -0.8314696, 0.55557024, -0.19509032
