# encoding: utf-8

require 'green_shoes'

Shoes.app width: 800, height: 600 do
  title('Методы решения СЛАУ', align: "center")
  flow do
    stack width: '30%' do
      @raw_matrix = edit_box
      @test_load = button 'Load test'
      @test_save = button 'Save test'
      s = @raw_matrix.text.split("\n")[0]

      def save_to(filename)
        return if not filename
        input = File.open(filename, 'w')
        t = @raw_matrix.text.split("\n")[0]
        matrix_size = t.split.length - 1
        if @init_approximation.text == '' or @init_approximation.text.split.length != matrix_size
          @init_approximation.text = '0 ' * matrix_size.to_i
        end
        s = "Matrix\n" + matrix_size.to_s + "\n" + @raw_matrix.text +
            "\nEpsilon\n" + @epsilon.text +
            "\nMaxIteration\n" + @max_iteration.text +
            "\nInitApproximation\n" + @init_approximation.text + "\n"
        input.write(s)
        input.close
      end

      @test_save.click {
        save_to ask_save_file
      }

      @test_load.click {
        filename = ask_open_file
        s = File.readlines(filename)
        n = s[1].to_i
        @raw_matrix.text = s[2, n].join('').chomp
        @epsilon.text = s[2 + n + 1].chomp
        @max_iteration.text = s[2 + n + 3].chomp
        @init_approximation.text = s[2 + n + 5].chomp
      }
      para 'epsilon'
      @epsilon = edit_line '0.001'
      para 'maxIteration'
      @max_iteration = edit_line '500'
      para 'initApproximation'
      @init_approximation = edit_line

      @solve = button 'Solve'
      generate = lambda do |gen_method|
        n = @size_to_generate.text.to_i
        return if not n
        h = Array.new(n){ Array.new(n + 1) }
        if gen_method.text == "Hilbert"
          h = Array.new(n){ Array.new(n + 1) }
          for i in 1..n
            for j in 1..n
              h[i - 1][j - 1] = 1.0 / (i + j - 1)
            end
            h[i - 1][n] = 1.0
          end
          h.map! { |a| a.join(' ')}
          @raw_matrix.text = h.join("\n")
        elsif gen_method.text == "DiagDom"
          for i in 1..n
            sum = 0
            for j in 1..n
              h[i - 1][j - 1] = rand(1..2**16)
              sum += h[i - 1][j - 1]
            end
            h[i - 1][i - 1] = sum + 1
            h[i - 1][n] = rand(1..2**16)
          end
          h.map! { |a| a.join(' ')}
          @raw_matrix.text = h.join("\n")
        end
      end
      @list_gen = list_box items: ["Manually", "Hilbert", "DiagDom"], choose: "Manually", &generate
      para 'Size of generating matrix'
      @size_to_generate = edit_line '3' do
        generate.call @list_gen
      end
    end
    @result = stack width: '70%' do
      @methods = ['gauss', 'seidel', 'jacobi', 'conjugate_gradient', 'relaxation']
      @methods.map! do |name|
        flow do
          @c = check
          para name
          catch_res = @res = edit_box
          @log = edit_box
          @error = button 'error' do
            return if @raw_matrix.text == ''
            rRight = @raw_matrix.text.split("\n").map { |row| row.split.pop}
            m = @raw_matrix.text.split("\n").map { |row| r = row.split; r.pop; r }
            v = catch_res.text.split(/\s/)
            n = rRight.length
            rOur = [0.0] * n
            sum = 0
            (0..n-1).each do |i|
              (0..n-1).each do |j|
                rOur[i] = rOur[i] + m[i][j].to_f * v[j].to_f
              end
              rRight[i] = (rRight[i].to_f - rOur[i]).abs.to_s
              sum += rRight[i].to_f ** 2
            end
            alert 'norm error ' + sum.to_s
            alert rRight.join("\n")
          end
        end
        @c.checked = true
        [ @c, name, @res, @log, @error ]
      end
      @solve.click do

        save_to('input')

        @result.append do
          for m in @methods
            m[2].text = m[3].text = ''
            method = m[1]

            next unless m[0].checked?
            `./gauss` if method == 'gauss'
            `./conjugate_gradient` if method == 'conjugate_gradient'
            `./relaxation` if method == 'relaxation'
            `java -jar Seidel.jar` if method == 'seidel'
            `java -jar Jacobi-1.0.jar` if method == 'jacobi'

            m[2].text = File.open('out_' + method).readlines.join if File.exist?('out_' + method)
            m[3].text = File.open('log_' + method).readlines.join if File.exist?('log_' + method)
          end
        end
      end
    end
  end
end
