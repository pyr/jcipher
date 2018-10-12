(ns jcipher.b64
  (:import java.util.Base64))

(defn ^bytes decode
  [^String input]
  (-> (Base64/getDecoder) (.decode input)))

(defn ^String encode
  [^bytes input]
  (-> (Base64/getEncoder) (.encodeToString input)))
