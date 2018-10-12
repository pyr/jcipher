(ns jcipher.pbe
  (:import java.security.SecureRandom
           javax.crypto.Cipher
           javax.crypto.SecretKey
           javax.crypto.SecretKeyFactory
           javax.crypto.spec.PBEKeySpec
           javax.crypto.spec.PBEParameterSpec))

(def key-iterations
  "Key obtention iterations, as per jasypt"
  1000)

(def salt-size
  "Salt size for PBEWithMD5AndDES"
  8)

(def ^SecretKeyFactory key-factory
  "Our secret key factory"
  (SecretKeyFactory/getInstance "PBEWithMD5AndDES" "SunJCE"))

(def ^Cipher cipher
  "The main cipher instance"
  (Cipher/getInstance "PBEWithMD5AndDES" "SunJCE"))

(def ^SecureRandom random
  (SecureRandom/getInstance "SHA1PRNG"))

(defn generate-salt
  []
  (let [salt (byte-array salt-size)]
    (.nextBytes random salt)
    salt))

(defn ^SecretKey generate-key
  [^String password]
  (let [key-spec ^PBEKeySpec (PBEKeySpec. (.toCharArray password))]
    (.generateSecret key-factory key-spec)))

(defn ^bytes initialize-cipher-salt
  [cipher mode ^SecretKey key]
  (let [salt       (generate-salt)
        param-spec (PBEParameterSpec. salt key-iterations) ]
    (.init ^Cipher cipher (int mode) key param-spec)
    salt))

(defn ^bytes run-encrypt
  [^Cipher cipher ^String input]
  (.doFinal cipher (.getBytes input "UTF-8")))

(defn ^bytes encrypt
  [^String password ^String input]
  (when (or (nil? password) (empty? password))
    (throw (IllegalArgumentException. "password cannot be null or empty")))
  (let [key    (generate-key password)
        salt   (initialize-cipher-salt cipher Cipher/ENCRYPT_MODE key)
        output (run-encrypt cipher input)]
    (byte-array (concat salt output))))

(defn ^{:tag "[B"} decrypt
  [password ^bytes input]
  (when (<= (count input) salt-size)
    (throw (IllegalArgumentException. "input payload too short")))
  (let [salt       (byte-array (take salt-size input))
        payload    (byte-array (drop salt-size input))
        key        (generate-key password)
        param-spec (PBEParameterSpec. salt key-iterations)]
    (.init ^Cipher cipher Cipher/DECRYPT_MODE key param-spec)
    (.doFinal cipher payload)))
