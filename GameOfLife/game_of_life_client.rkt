; Implementation of Conway's Game of Life
; Bucky Kittinger
; 11/20/13

#lang racket/gui
(require 2htdp/image)
(require 2htdp/universe)
(require test-engine/racket-gui)
(require racket/tcp)

(define-values (c-in c-out)(tcp-connect "localhost" 10000))
(file-stream-buffer-mode c-in 'block)
(file-stream-buffer-mode c-out 'block)
(display "wake\nul" c-out)

; constant for the cell size
(define CELL_SIZE 10)

; a cell has a state and a position
; state: symbol 'alive, 'dead
; pos: integer
(define-struct cell (state pos) #:transparent)

;examples of cell
(make-cell 'alive 0)
(make-cell 'dead 1028)

; function template for cell
;(define (func-for-cell c)
;  (...(cell-state c)...(cell-pos c)...))

; a list-of-cells is either 
; - empty or  
; - a `cell` followed by a list-of-cells

;examples
empty
(cons (make-cell 'alive 0) empty)
(list (make-cell 'dead 0) (make-cell 'alive 1))

; template for list-of-cells
;(define (func-for-loc loc)
;  (cond [(empty? loc) ...]
;        [(cons? loc) (... (first loc)
;                     ... (rest loc))]))

; configuration is
; a list-of-cells
; a boolean: determine if the game is over
; a boolean: determine if the updates should loop
(define-struct configuration (cells is-done? loop-updates? delay))

; constants for the board size
(define BOARD_WIDTH 64)
(define BOARD_HEIGHT 64)

; tests for alive?
(check-expect (alive? (make-cell 'alive 0)) #t)
(check-expect (alive? (make-cell 'dead 0)) #f)

; alive?: cell -> boolean
; given `c` it will determine if it is alive
(define (alive? c)
  (let {[c-state (cell-state c)]}
    (if (symbol=? c-state 'alive) #t #f)))

; tests for dead?
(check-expect (dead? (make-cell 'alive 0)) #f)
(check-expect (dead? (make-cell 'dead 0)) #t)

; dead?: cell -> boolean
; given `c` it will determine if it is dead
(define (dead? c)
  (not (alive? c)))

; tests for x-comp 
(check-expect (x-comp (make-cell 'alive 0)) 0)
(check-expect (x-comp (make-cell 'alive 1)) 1)
(check-expect (x-comp (make-cell 'alive 64)) 0)
(check-expect (x-comp (make-cell 'alive 100)) 36)
(check-expect (x-comp (make-cell 'alive 4095)) 63)
(check-expect (y-comp (make-cell 'alive 4096)) 64)

; x-comp: cell -> number
; given `c` it will get the x component from a cell
; precondition: all cells should have a nonnegative position
(define (x-comp c)
  (modulo (cell-pos c) BOARD_WIDTH))

; tests for x-comp 
(check-expect (y-comp (make-cell 'alive 0)) 0)
(check-expect (y-comp (make-cell 'alive 1)) 0)
(check-expect (y-comp (make-cell 'alive 64)) 1)
(check-expect (y-comp (make-cell 'alive 100)) 1)
(check-expect (y-comp (make-cell 'alive 4095)) 63)
(check-expect (y-comp (make-cell 'alive 4096)) 64)

; dead?: cell -> number
; given `c` it will get the y component from a cell
; precondition: all cells should have a nonnegative position
(define (y-comp c)
  (quotient (cell-pos c) BOARD_HEIGHT))

; initial cell-list for the game
(define cell-list (build-list (* BOARD_WIDTH BOARD_HEIGHT) (λ (pos) (make-cell 'dead pos))))

; draw-cell: cell, image -> image
; given `cell` it will draw it onto `bg`
(define (draw-cell cell bg)
  (let {[x-off (- (/ (* BOARD_WIDTH CELL_SIZE) 2) (/ CELL_SIZE 2))]
        [y-off (- (/ (* BOARD_HEIGHT CELL_SIZE) 2) (/ CELL_SIZE 2))]}
    (if (alive? cell) (overlay/offset (square CELL_SIZE 'solid 'green)
        (- x-off (* (x-comp cell) CELL_SIZE))
        (- y-off (* (y-comp cell) CELL_SIZE))
        bg)
    bg)))

; draw-cells: list-of-cells, image -> image
; given `loc` it will draw all the elements onto `bg`
(define (draw-cells loc bg)
  (cond [(empty? loc) bg]
        [(cons? loc) (draw-cell (first loc) (draw-cells (rest loc) bg))]))

#|
; this caused the execution to slow down dramatically
(define (create-row los)
  (cond [(empty? los) empty-image]
        [(cons? los) (beside (first los) (create-row (rest los)))]))

(define (create-col lor)
  (cond [(empty? lor) empty-image]
        [(cons? lor) (above (first lor) (create-col (rest lor)))]))
                         
(define (draw-grid)
  (let* {[row (create-row (build-list 64 (λ(x) (square CELL_SIZE 'outline 'gray))))]
         [grid (create-col (build-list 64 (λ(x) row)))]}
    grid))
(define grid (draw-grid))
(define background (rectangle (* CELL_SIZE BOARD_WIDTH) (* CELL_SIZE BOARD_HEIGHT) 'solid 'black))
|#

; draw-background: color, number, string -> image
; given `gen` and `msg` it will create a background with the color `col` 
; with the generation updated to `gen` and the `msg` posted at the bottom
(define (draw-background col gen msg)
  (let* {[bkg (rectangle (* CELL_SIZE BOARD_WIDTH) (+ (* CELL_SIZE BOARD_HEIGHT) 60) 'solid col)]
        [bottom (rectangle (* CELL_SIZE BOARD_WIDTH) 30 'solid 'grey)]
        [txt (overlay/offset (text msg 20 'black) 5 (* CELL_SIZE BOARD_HEIGHT) bottom)]
        [counter (overlay/offset (text (string-append "GENERATION: " (number->string gen)) 20 'black) 200 (* CELL_SIZE BOARD_HEIGHT) txt)]}
    (overlay/offset bottom
                    0
                    (* CELL_SIZE BOARD_HEIGHT)
                    counter)))

             
; draw-configuration: configuration -> image
; given `con` it will draw a representation of the configuration
(define (draw-configuration con)
  (let {[bkg (rectangle (* CELL_SIZE BOARD_WIDTH) (* CELL_SIZE BOARD_HEIGHT) 'solid 'black)]}
    (draw-cells (configuration-cells con) bkg)))

; draw-final: configuration -> image
(define (draw-final con)
  (let {[txt (text "GAME OF LIFE SHUTDOWN" 30 'solid 'green)]
        [img (draw-configuration con)]}
    (overlay txt img)))

(define (find-cell loc x y)
  (let* {[pos (+ (* (quotient y CELL_SIZE) BOARD_HEIGHT) (quotient x CELL_SIZE))]
        [found-cells (filter (λ (c) (= (cell-pos c) pos)) loc)]}
    (cond [(empty? found-cells) (make-cell 'dead pos)]
          [(cons? found-cells) (first found-cells)])))

(define (toggle-cell cell)
  (make-cell (if (alive? cell) 'dead 'alive) (cell-pos cell)))

(define done #f)


(define (cells->string cells)
  (cells->string-help cells ""))

(define (cells->string-help cells str-so-far)
  (cond [(empty? cells) (string-append str-so-far (string #\nul))]
        [(cons? cells) (let* {[c (first cells)]
                              [str (if (alive? c) "t" "f")]}
                         (cells->string-help (rest cells) (string-append str-so-far str)))]))


(define (conf->string conf)
  (let {[cells (configuration-cells conf)]}
    (cells->string cells)))

(define (send-to-server str)
  (let {[len (string-length str)]}
    (write-string str c-out)))
    ;(flush-output c-out)))

(define (string->cells str)
  (string->cells-help str 0))

(define (string->cells-help str index)
  (cond [(string=? "" str) empty]
        [else 
         (let {[state (if (char=? (string-ref str 0) #\t) 'alive 'dead)]}
           (cons (make-cell state index) (string->cells-help (substring str 1) (add1 index))))]))
           ;(string->cells-help (substring str 1) (add1 index) (cons (make-cell state index) list-so-far)))]))
            ;(cons (make-cell state index) (string->cells-help (substring str 1) (add1 index))))]))

(define (update-conf new-cells old-conf)
  (make-configuration new-cells (configuration-is-done? old-conf) (configuration-loop-updates? old-conf)))


#|
(define (read-from-server port)
  (let* {[p (peek-char port)]}
    (cond [(equal? p #\nul) ""]
          [else (let {[r (read-char port)]}
                  (string-append (string p) (read-from-server port)))])))
|#

(define (read-from-server port)
  (read-from-server-help port ""))

(define (read-from-server-help port read-so-far)
  (let {[r (read-char port)]}
    (cond [(equal? r #\nul) read-so-far]
          [else (read-from-server-help port (string-append read-so-far (string r)))])))

; flush-input!: port
; procedure to consume all the bytes left in the input port
(define (flush-input! port)
  (let* {[bstr (bytes 0)] 
         [num-read (read-bytes-avail!* bstr port)]}
    (cond [(zero? num-read) ]
          [else (flush-input! port)])))


               
(define padding (build-string 256 (λ(x) #\nul)))

(define (write-to-server str port)
  (write-string str port)
  (flush-output port))

(define (handle-receive conf)
  (let {[snd (write-to-server (string-append "p" (cells->string (configuration-cells conf))) c-out)]}
    conf))

(define (handle-exit conf)
  (let {[snd (write-to-server "e" c-out)]
        [close (close-output-port c-out)]}
    (make-configuration (configuration-cells conf) #t (configuration-loop-updates? conf) (configuration-delay conf))))

(define (handle-update conf)
  (let {[snd (write-to-server (string-append "u" padding) c-out)]}
    (make-configuration (string->cells (read-from-server c-in)) 
                        (configuration-is-done? conf) 
                        (configuration-loop-updates? conf)
                        (configuration-delay conf))))

(define (handle-clear conf)
  (let {[snd (write-to-server (string-append "c" padding) c-out)]}
    conf))

(define (handle-loop conf)
  (make-configuration (configuration-cells conf) (configuration-is-done? conf) (not (configuration-loop-updates? conf)) (configuration-delay conf)))

(define (keybos conf key)
  (cond [(string=? key "escape") (handle-exit conf)]
        [(string=? key "p") (handle-receive conf)]
        [(string=? key "u") (handle-update conf)] 
        [(string=? key "c") (handle-clear conf)]
        [(string=? key "l") (handle-loop conf)]
        [else conf]))


(define (on-exit conf)
  (configuration-is-done? conf))

(define (replace-cell cell loc)
  (cond [(empty? loc) (cons cell empty)]
        [(cons? loc) (if (= (cell-pos (first loc)) (cell-pos cell)) 
                         (cons cell (rest loc))
                         (cons (first loc) (replace-cell cell (rest loc))))]))


(define (handle-mouse conf x y e)
  (let {[loc (configuration-cells conf)]
        [done? (configuration-is-done? conf)]
        [loop? (configuration-loop-updates? conf)]
        [delay (configuration-delay conf)]}
    (cond [(string=? e "button-down")
           (let {[cell (find-cell loc x y)]}
             (make-configuration (replace-cell (toggle-cell cell) loc) done? loop? delay))]
          [else conf])))

(define (conf-set-delay delay conf)
  (make-configuration (configuration-cells conf) (configuration-is-done? conf) (configuration-loop-updates? conf) delay))

(define (handle-on-tick conf)
  (let {[delay (configuration-delay conf)]}
    (cond [(false? (configuration-loop-updates? conf)) conf]
        [else (if (> delay 1) 
                  (handle-update (conf-set-delay 0 conf)) 
                  (conf-set-delay (add1 delay) conf))])))

(define init-conf (make-configuration cell-list #f #f 0))

(big-bang init-conf
          [on-key keybos]
          [on-tick handle-on-tick]
          [to-draw draw-configuration]
          [on-mouse handle-mouse]
          [stop-when on-exit])
