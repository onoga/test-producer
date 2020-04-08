(ns test-producer.core
  (:gen-class)
  (:require [clojure.string :as string]
            [mount.core :as mount]
            [clojure.tools.cli :as cli]
            [clojure.edn :as edn]
            [test-producer.api.core :as api]))


(def cli-options
  [;; Options
   ["-h" "--help" "show help"]
   ["-g" "--group-id GROUP_ID" "Kafka group.id"
    :default "test-producer"]
   ["-b" "--bootstrap-servers BOOTSTRAP_SERVERS" "Kafka bootstrap.servers"
    :default "localhost:9092"]
   ["-l" "--host HOST" "Http server host"
    :default "0.0.0.0"]
   ["-p" "--port PORT" "Http server port"
    :parse-fn edn/read-string
    :default 8080]])


(defn -main
  "Application's entry point"
  [& args]
  (let [{:keys [options summary errors]} (cli/parse-opts args cli-options)]
    (clojure.pprint/pprint options)
    (cond
      errors
      (do
        (println "Errors:"
                 (string/join "\n\t" errors))
        1)

      (:help options)
      (println summary)

      :else
      (do
        (-> (mount/with-args options)
            (mount/start))
        (api/start options)))))
