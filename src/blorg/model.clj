(ns blorg.model
  (:require [cheshire.core :as json])
  (:require [clojure.java.io :as io])
  (:import [java.util Timer TimerTask]))

(def posts (atom nil))

(defn add-post [post]
  (swap! posts (fn [old-posts] (conj old-posts post))))

(defn save-posts []
  (spit "posts.json" (json/generate-string @posts)))

; .exists is a method of Java's File
(defn load-posts-file [file]
  (if (.exists (io/file file)) ; Maksim added - was crashing on missing file
    (json/parse-string (slurp file) true)
    []))

(defn load-posts []
  (reset! posts (load-posts-file "posts.json")))

(def timer (atom nil))

(defn start-save-timer []
  (reset! timer (Timer.))
  (.schedule @timer (proxy [TimerTask] [] (run [] (save-posts))) 0 5000))

