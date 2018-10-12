(ns jcipher.main
  (:require [jcipher.pbe       :as pbe]
            [jcipher.b64       :as b64]
            [clojure.tools.cli :refer [cli]])
  (:gen-class))

(defn encrypt
  [key input]
  (b64/encode (pbe/encrypt key input)))

(defn decrypt
  [key input]
  (String. (pbe/decrypt key (b64/decode input)) "UTF-8"))

(defn get-cli
  "Parse command line arguments and ensure we return a proper structure."
  [args]
  (try
    (cli args
         ["-h" "--help"    "Show Help"          :default false :flag true]
         ["-e" "--encrypt" "Perform encryption" :default false :flag true]
         ["-k" "--key"     "Key password"       :default nil])
    (catch Exception e
      (binding [*out* *err*]
        (println "Could not parse arguments: " (.getMessage e)))
      (System/exit 1))))

(defn usage
  [banner exit-value]
  (binding [*out* (if (pos? exit-value) *out* *err*)]
    (println "Usage: jcipher [-e] -k KEY PAYLOAD")
    (println "")
    (println banner))
  (System/exit exit-value))

(defn -main
  [& args]
  (let [[opts args banner] (get-cli args)
        encrypt?           (:encrypt opts)
        help?              (:help opts)
        key                (:key opts)
        payload            (first args)]
    (cond
      help?          (usage banner 0)
      (nil? payload) (usage banner 1)
      encrypt?       (println (encrypt key payload))
      :else          (println (decrypt key payload)))
    (System/exit 0)))
