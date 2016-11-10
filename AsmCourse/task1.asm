extern __imp__MessageBoxA@16
extern __imp__ExitProcess@4
extern __imp__wsprintfA 
extern _printf

section .text
global _main
_main: 
mov ebx, [esp + 8]
mov edx, [ebx + 4]
mov dword [flags_ptr], edx
mov edx, [ebx + 8]
mov dword [number_ptr], edx

;push dword [flags_ptr]
;call _printf
;pop edx

mov ebx, 0
mov edx, 0

mov cx, 0
mov ch, 0; lengthState
mov esi, 0; p




 
mov eax, dword [flags_ptr];
mov edx, 0; edx - mask
		
while1:
	mov cl, byte [eax + esi]
	inc esi
	cmp cl, 0
	je end_while1

	cmp ch, 0
	je mask_state

	length_state:
		push eax
		
		movzx eax, cl
		sub eax, '0'
		imul ebx, [length], 10
		add ebx, eax
		mov word [length],  bx

		pop eax
		jmp while1
	mask_state:
		cmp cl, '1'
		jl not_length_begin
		cmp cl, '9'
		jg not_length_begin

		mov ch, 1
		sub cl, '0'
		mov byte [length], cl
		add cl, '0'

		not_length_begin:

		cmp cl, '+'
		je plus
		cmp cl, '-'
		je minus
		cmp cl, '0'
		je zero
		cmp cl, ' '
		je space
		jmp end_case1
		plus:
			or edx, 1
			jmp end_case1
		minus:
			or edx, 2
			jmp end_case1
		zero:
			or edx, 4
			jmp end_case1
		space:
			or edx, 8
			jmp end_case1
		end_case1:
			jmp while1
end_while1:

mov word [mask], dx

mov eax, dword [number_ptr]
mov bl, byte [eax]

cmp bl, '-'
jne not_first_minus
mov word [negate], 1
inc eax

not_first_minus:

push eax
mov edx, number_hex
; eax - number_input, edx - number_hex
mov ecx, 0; real_len
while_len:
	 mov bl, byte [eax]
	 cmp bl, 0
	 je end_while_len
	 ; compute symbol
	 or bl, 0x20
	 cmp bl, '0'
	 jl letter_label
	 cmp bl, '9'
	 jg letter_label
	 digit_label:
		sub bl, '0'
		mov byte [edx], bl
		jmp end_compute_symbol
	 letter_label:
		sub bl, 'a'
		add bl, 10
		mov byte [edx], bl
	 end_compute_symbol:

	 inc edx
	 inc eax
	 inc ecx

	 jmp while_len
end_while_len:
mov word [real_len], cx
pop eax

mov eax, 0
mov ax, word [real_len]
cmp eax, 32
jne not_big_number
mov bl, byte [number_hex]
cmp bl, 8
jl  not_big_number

mov byte [add1], 1
xor byte [negate], 1
mov eax, number_hex
mov ecx, 0
mov cx, word [real_len]
mov ebx, 0

invert_loop:
	cmp ecx, 0
	je end_invert_loop
	mov bl, 15
	sub bl, byte [eax]
	mov byte [eax], bl
	
	dec ecx
	inc eax
	jmp invert_loop
end_invert_loop:

not_big_number:

mov eax, number_hex
mov ecx, 0
mov cx, word [real_len]
mov ebx, 0
to_dec_loop:
	cmp ecx, 0
	je end_to_dec_loop
	mov bl, byte [eax]

	pusha
	call multiply
	popa
	
	pusha
	push ebx
	call addition
	popa

	dec ecx
	inc eax	
	jmp to_dec_loop
end_to_dec_loop:

mov eax, 0
mov ax, word [add1]
cmp eax, 1
jne not_add1

pusha 
push 1
call addition
popa

not_add1:

mov ecx, 0; ecx - p
mov edx, number_output
mov eax, number_dec
add eax, 255
mov esi, 255
number_output_loop:
	mov bl, byte [eax]
	cmp bl, 0
	jne good_number_output
	cmp ecx, 0
	jne good_number_output
	jmp not_good_number_ouput
	good_number_output:
		add bl, '0'

		mov byte [edx], bl
		inc edx
		inc ecx
	not_good_number_ouput:
	dec esi
	dec eax
	cmp esi, 0
	jne number_output_loop
end_number_output_loop:

mov bl, byte [eax]
add bl, '0'
mov byte [edx], bl
inc edx
inc ecx

cmp bl, byte '0'
jne not_minus_zero

cmp ecx, 1
jne not_minus_zero

mov word [negate], 0

not_minus_zero:


mov word [length_dec], cx	

mov ecx, 0; ecx - p

cmp word [negate], 1
je negate_flag
mov bx, word [mask]
and bx, 1
cmp bx, 0
jne plus_flag
mov bx, word [mask]
and bx, 8
cmp bx, 0
jne space_flag

jmp end_first_flag_states

negate_flag:
	mov word [first_flag], 1
	mov byte [add_char], '-'
	jmp end_first_flag_states
plus_flag:
	mov word [first_flag], 1
	mov byte [add_char], '+'
	jmp end_first_flag_states
space_flag:
	mov word [first_flag], 1
	mov byte [add_char], ' '
	jmp end_first_flag_states
end_first_flag_states:

mov eax, 0
mov ax, word [length]
sub ax, word [first_flag]
sub ax, word [length_dec]
mov word [count], ax

mov ax, word [mask]


mov bx, ax
and bx, 4
cmp bx, 0
jne zero_flag



mov bx, ax
and bx, 2
cmp bx, 0
jne minus_flag


jmp no_flag
zero_flag:
	mov eax, result

	cmp byte [add_char], 0
	je no_add_zero
		mov ebx, 0
		mov bl, byte [add_char]
		mov byte [eax], bl
		inc eax
	no_add_zero:
	
	mov ecx, 0
	mov cx, word [count]
	print0zero:
		cmp cx, 0
		jle end_print0zero
		dec cx
		
		mov byte [eax], '0'
		inc eax
		jmp print0zero
	end_print0zero;

	mov ecx, number_output
	mov esi, 0
	mov si, word [length_dec]
	print_final_number_zero:
		cmp si, 0
		jle end_print_final_number_zero
		dec si

		mov bl, byte [ecx]
		mov byte [eax], bl

		inc ecx
		inc eax

		jmp print_final_number_zero
	end_print_final_number_zero:

	jmp end_flags
minus_flag:
	mov eax, result

	cmp byte [add_char], 0
	je no_add_minus
		mov ebx, 0
		mov bl, byte [add_char]
		mov byte [eax], bl
		inc eax
	no_add_minus:

	mov ecx, number_output
	mov esi, 0
	mov si, word [length_dec]
	print_final_number_minus:
		cmp si, 0
		jle end_print_final_number_minus
		dec si

		mov bl, byte [ecx]
		mov byte [eax], bl

		inc ecx
		inc eax

		jmp print_final_number_minus
	end_print_final_number_minus:

	mov ecx, 0
	mov cx, word [count]
	printspaceminus:
		cmp cx, 0
		jle end_printspaceminus
		dec cx
		
		mov byte [eax], ' '
		inc eax
		jmp printspaceminus
	end_printspaceminus:

	jmp end_flags
no_flag:
	mov eax, result

	mov ecx, 0
	mov cx, word [count]
	printspacenoflag:
		cmp cx, 0
		jle end_printspacenoflag
		dec cx
		
		mov byte [eax], ' '
		inc eax
		jmp printspacenoflag
	end_printspacenoflag:

	cmp byte [add_char], 0
	je no_add_noflag
		mov ebx, 0
		mov bl, byte [add_char]
		mov byte [eax], bl
		inc eax
	no_add_noflag:

	mov ecx, number_output
	mov esi, 0
	mov si, word [length_dec]
	print_final_number_noflag:
		cmp si, 0
		jle end_print_final_number_noflag
		dec si

		mov bl, byte [ecx]
		mov byte [eax], bl

		inc ecx
		inc eax

		jmp print_final_number_noflag
	end_print_final_number_noflag:

	jmp end_flags
end_flags:

;!!!!!!!!!
pusha 
push result
call _printf
pop ebx
popa

;!!!!!!!!!!!!!!!!!!!!!!!!!! TO DEL
;mov eax, 0
;mov ax, word [real_len]
;push eax
;push edx
;push title
;call _printf

;push 40h ; константа из WinAPI: "info icon"
;push title; заголовок окна, определён внизу
;push title; текст окна
;push 0; хэндл окна, у нас его нет
;call [__imp__MessageBoxA@16]
;!!!!!!!!!!!!!!!!!!!!!!!!!!!1

push 0 ; код возврата
call [__imp__ExitProcess@4]

multiply:
	mov word [cr], 0
	mov eax, number_dec
	mov ecx, 256
	multiply_loop:
		cmp ecx, 0
		je end_multiply_loop
		mov bl, byte [eax]
		
		pusha
		mov esi, eax
		mov eax, 0
		mov ax, word [cr]
		mov edx, 0
		movzx edx, bl
		imul edx, 16
		add eax, edx
		mov edx, 0
		mov ecx, 10
		idiv ecx
		mov byte [esi], dl
		mov word [cr], ax
		popa

		inc eax
		dec ecx	
		jmp multiply_loop
	end_multiply_loop:
	
	ret

addition:
	pop esi ; return adress at the top
	pop ebx
	push esi

	mov word [cr], bx
	mov ebx, 0
	mov eax, number_dec
	mov ecx, 256

	addition_loop:
		cmp ecx, 0
		je end_addition_loop
		mov bl, byte [eax]
		
	    pusha
		mov esi, eax
		mov eax, 0
		mov edx, 0
		mov ax, word [cr]
		add ax, bx
		mov ecx, 10
		
		div ecx

		mov byte [esi], dl
		mov word [cr],ax
		popa

		inc eax
		dec ecx	
		jmp addition_loop
	end_addition_loop:

	ret

section .bss
number_hex resb 256
number_dec resb 256
number_output resb 256
result	   resb 256


section .data
flags_ptr  dd 0
number_ptr dd 0
count	   dw 0
length_res dw 0
add1	   dw 0
first_flag dw 0
cr		   dw 0
length     dw 0
negate     dw 0
length_dec dw 0
real_len   dw 0
add_char   db 0
flags	   db " 7", 0
;number_input db "-12", 0
number_input db "ffffffffffffffffffffffffffffffff", 0
mask       dw 0
title      db "mask : %d, length : %d", 0
text       db "Hello, world!", 0
debug	   db "%c", 0
print_string db "%s", 0

end 