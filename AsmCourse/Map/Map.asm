include 'proc16.inc'
use16
org 100h
    jmp start
;---Данные-------------------------
mem1  dw 4096 dup(0)
mem2  dw 4096 dup(0)
N     dw 64
cnt   dw 0
zer   dw 0
key   dw 64   dup(0)
keytmp dw 64  dup(0)
valtmp dw 64  dup(0)
len   dw 2   dup(0)
tmp   dw 2   dup(0)
val   dw 64   dup(0)
req   dw 64  dup(0)
not_this_string db 'There are no such key in map$'
;----------------------------------
;---Макросы------------------------
macro exit_app
{
    mov ax,4C00h
    int 21h
}

;возвращает длину строки в ax
proc  get_length stdcall uses bx dx, s:WORD
   mov ax, 0
   mov bx, [s]
get_length_loop:
   mov dl, [bx]
   inc bx
   inc ax
   cmp dl, '$'
   jne get_length_loop
   dec ax
   ret
endp

proc  input_str stdcall uses ax bx cx, s:WORD
    mov bx, [s]
input_str_loop:
    mov ah,01h		;Функция DOS 01h - ввод символа
    int 21h		;Введённый символ помещается в AL
    cmp al,' '
    je	input_str_finish
    cmp al,13
    je	input_str_finish
    mov [bx],al
    inc bx
    jmp input_str_loop
input_str_finish:    
    mov byte[bx],'$'
    ret
endp

proc  print_str stdcall uses ax bx cx dx, s:WORD
    mov bx, [s]
print_str_loop:
    mov dl, [bx]
    inc bx
    cmp dl, '$'
    je	print_str_finish
    mov ah,2		;Функция DOS 02h - вывод символа
    int 21h		;Введённый символ помещается в AL
    jmp print_str_loop
print_str_finish:    
    ret
endp


proc  print_end_line stdcall uses ax dx
    mov ah,2		;Функция DOS 02h - вывод символа
    mov dl,13		;Символ CR
    int 21h
    mov dl,10		;Символ LF
    int 21h
    ret 		;Возврат из процедуры
endp

proc  print_space stdcall uses ax dx
    mov ah,2		;Функция DOS 02h - вывод символа
    mov dl,' '		 ;Символ CR
    int 21h
    ret 		;Возврат из процедуры
endp
				   
proc  input stdcall uses ax bx cx dx di si
    mov di, mem1	  ;итератор количества элементов в мапе
    mov si, mem2
input_map_loop:
    stdcall input_str, di
    stdcall get_length, di
    cmp ax, 0
    je	 end_input_map_loop
    stdcall input_str, si
    stdcall print_end_line
    mov  bx, [cnt]
    imul bx,bx,2
    mov  [key + bx], di
    mov  [val + bx], si
    add  di, [N]
    add  si, [N]
    inc[cnt]
    jmp input_map_loop
end_input_map_loop:  
    stdcall print_end_line  
    ret
endp

proc  print_map stdcall uses ax bx cx dx di si
    mov di, key 	 ;итератор количества элементов в мапе
    mov si, val
    mov bx, [cnt]
    cmp bx, 0
    je	end_print_map_loop
print_map_loop:
    stdcall print_str, [di]
    stdcall print_space
    stdcall print_str, [si]
    stdcall print_end_line
    dec  bx
    cmp  bx, 0
    je	 end_print_map_loop
    add  di,2
    add  si,2
    jmp print_map_loop
end_print_map_loop:    
    ret
endp

;в ax возвращается результат -1, 0, 1
;сортит так что сначала идут с меньшим количеством символов, а внутри лексикографически
proc  compare_str uses bx cx dx di si, s1:WORD, s2:WORD
local l1 dw 0
local l2 dw 0
    stdcall get_length, [s1]
    mov [l1], ax
    stdcall get_length, [s2]
    mov [l2], ax
    
    mov ax, [l1]
    mov bx, [l2]
    cmp ax, bx
    je	equal_length
    jl	less
    jmp more
less:
    mov ax, -1
    ret
more:  
    mov ax, 1
    ret  
equal_length:
    mov di, [s1]
    mov si, [s2]
    mov cx, 0
compare_str_loop:
    mov bl, [di]
    mov bh, [si]
    cmp bl, bh
    jl	less
    jg	more
    inc di
    inc si
    inc cx
    cmp cx, [l1]
    je	end_compare_str_loop
    jmp compare_str_loop
end_compare_str_loop:
    mov ax, 0
    ret
endp

proc  merge_sort stdcall uses ax bx cx dx di si, l:WORD, r:WORD
local m dw 0
local count dw 0
local half_count dw 0
    mov cx, [r]
ms_count_loop:
    cmp cx, [l]
    je end_ms_count_loop
    inc [count]
    dec cx
    jmp ms_count_loop
end_ms_count_loop:

    ;найдем количество элементов в половинном массиве
    mov ax, [count]
    mov bx, 2
    mov dx, 0
    div bx 
    mov [half_count], ax
    add ax, [l]
    mov [m], ax
    
    ;теперь m и r - корректно готовы
    mov cx, [l]
    mov dx, [r]
    
    dec dx     ;check r - l <= 1
    cmp cx,dx
    jge  sorted
    inc dx
    
    stdcall merge_sort, [l], [m]
    stdcall merge_sort, [m], [r]
   
    
    imul di, [l], 2
    mov  [l], di
    imul si, [m], 2
    mov  [m], si
    mov  bx, di
    imul cx, [r], 2
    mov  [r], cx   
both_merge_loop:
    cmp di, [m]
    je	both_merge_loop_end
    cmp si, [r]
    je	both_merge_loop_end
    mov cx, [key + di]
    mov dx, [key + si]
    stdcall compare_str, cx, dx 
    cmp ax, 0
    jl	less_or_eq_both_merge
    jg	more_both_merge
    je	less_or_eq_both_merge
less_or_eq_both_merge:
    mov cx, [key + di]
    mov [keytmp + bx], cx
    mov cx, [val + di]
    mov [valtmp + bx], cx
    add bx,2
    add di,2
    jmp both_merge_loop 
more_both_merge:
    mov cx, [key + si]
    mov [keytmp + bx], cx
    mov cx, [val + si]
    mov [valtmp + bx], cx
    add bx,2
    add si,2
    jmp both_merge_loop
both_merge_loop_end:

di_merge_loop:
    cmp di, [m]
    je	si_merge_loop
    mov cx, [key + di]
    mov [keytmp + bx], cx
    mov cx, [val + di]
    mov [valtmp + bx], cx
    add bx,2
    add di,2
    jmp di_merge_loop 
    
si_merge_loop:
    cmp si, [r]
    je	merged
    mov cx, [key + si]
    mov [keytmp + bx], cx
    mov cx, [val + si]
    mov [valtmp + bx], cx
    add bx,2
    add si,2
    jmp si_merge_loop
merged:
    mov bx, [l]
merged_loop:
    cmp bx, [r]
    je	sorted
    mov cx, [keytmp + bx]
    mov [key + bx], cx
    mov cx, [valtmp + bx]
    mov [val + bx], cx
    add bx,2
    jmp merged_loop
sorted:    
    ret
endp 

proc bin_search stdcall uses ax bx cx dx, l:WORD, r:WORD
local m dw 0
local count dw 0
local half_count dw 0
    mov cx, [r]
bs_count_loop:
    cmp cx, [l]
    je end_bs_count_loop
    inc [count]
    dec cx
    jmp bs_count_loop
end_bs_count_loop:

    ;найдем количество элементов в половинном массиве
    mov ax, [count]
    mov bx, 2
    mov dx, 0
    div bx 
    mov [half_count], ax
    add ax, [l]
    mov [m], ax
    
    ;теперь m и r - корректно готовы
    mov cx, [l]
    mov dx, [r]
    
   
    dec dx     ;check r - l <= 1
    cmp cx,dx
    je found
    inc dx

    imul si, [m], 2
    mov cx, [key + si]
    stdcall compare_str, cx, req
    cmp ax, 0
    jle cur_less_or_eq
    jg	cur_more    
cur_less_or_eq:
    stdcall bin_search, [m], [r]
    ret
cur_more:    
    stdcall bin_search, [l], [m]
    ret
found:
    imul si, [l], 2
    mov cx, [key + si]
    stdcall compare_str, cx, req
    cmp ax, 0
    je	exist
    jne not_exist
exist:
    mov cx, [val + si]
    stdcall print_str, cx
    ret
not_exist:
    stdcall print_str, not_this_string
    ret
endp

proc ans_requests stdcall uses ax bx cx dx
ans_requests_loop:
    stdcall input_str, req
    stdcall print_end_line
    stdcall get_length, req
    cmp ax, 0
    je	 end_ans_requests_loop
    stdcall bin_search, [zer], [cnt]
    stdcall print_end_line
    jmp ans_requests_loop
end_ans_requests_loop:
    stdcall print_end_line 
    ret 
endp
;----------------------------------
start:
    stdcall input
    stdcall merge_sort, [zer], [cnt] 
    stdcall ans_requests
    exit_app
;----------------------------------