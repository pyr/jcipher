(def graal-path (partial str (System/getenv "GRAAL_HOME")))

(defproject jcipher "0.1.0"
  :description "jasypt utility cli"
  :url "https://github.com/pyr/jcipher"
  :plugins [[io.taylorwood/lein-native-image "0.3.0"]]
  :native-image {:name "jcipher"
                 :opts ["-Dclojure.compiler.direct-linking=true"
                        "--report-unsupported-elements-at-runtime"
                        "--enable-all-security-services"]
                 :graal-bin ~(graal-path "/bin/native-image")}
  :license {:name "MIT/ISC License"}
  :main jcipher.main
  :profiles {:uberjar {:aot :all}}
  :dependencies [[org.clojure/clojure   "1.9.0"]
                 [org.clojure/tools.cli "0.4.1"]])
