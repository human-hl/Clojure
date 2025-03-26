;; Виртуальный плоттер на Clojure в функциональном стиле

(def initial-state {:position {:x 0 :y 0}
                    :angle 0.0
                    :color :black
                    :carriage :up})

(defn radian [angle]
  (* angle (/ Math/PI 180)))

(defn calc-new-position [distance angle position]
  (let [angle-rad (radian angle)
        x (+ (:x position) (* distance (Math/cos angle-rad)))
        y (+ (:y position) (* distance (Math/sin angle-rad)))]
    {:x (Math/round x) :y (Math/round y)}))

(defn move [distance state]
  (if (map? state)  
    (let [new-pos (calc-new-position distance (:angle state) (:position state))
          new-state (assoc state :position new-pos)]
      (if (= (:carriage state) :down)
        (do
          (println (str "Рисуем линию от " (:position state) " до " new-pos " используя " (:color state)))
          new-state)
        (do
          (println (str "Перемещаем на " distance))
          new-state)))
    state))  
(defn turn [angle state]
  (if (map? state)
    (let [new-angle (mod (+ (:angle state) angle) 360)]
      (assoc state :angle new-angle))
    state))

(defn carriage-up [state]
  (if (map? state)
    (assoc state :carriage :up)
    state))

(defn carriage-down [state]
  (if (map? state)
    (assoc state :carriage :down)
    state))

(defn set-color [color state]
  (if (map? state)
    (assoc state :color color)
    state))

(defn set-position [x y state]
  (if (map? state)
    (assoc state :position {:x x :y y})
    state))

(defn draw-triangle [size state]
  (-> state
      carriage-down
      (move size)
      (turn 120)
      (move size)
      (turn 120)
      (move size)
      (turn 120)
      carriage-up))

(defn draw-square [size state]
  (-> state
      carriage-down
      (move size)
      (turn 90)
      (move size)
      (turn 90)
      (move size)
      (turn 90)
      (move size)
      (turn 90)
      carriage-up))

(let [state (-> initial-state
                (draw-triangle 100)
                (set-position 10 10)
                (set-color :red)
                (draw-square 80))]
  (println "Итоговое состояние:" state))

