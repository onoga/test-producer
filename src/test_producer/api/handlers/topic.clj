(ns test-producer.api.handlers.topic
  (:require [test-producer.kafka.producer :as kafka-producer]
            [cheshire.core :as json]
            [zlib-tiny.core :as zlib]))


(defn ->zip-json [message]
  (-> message
      json/encode
      zlib/str->bytes
      zlib/deflate))


(defn post [{{:keys [topic encoder]} :path-params
             message                 :body-params}]
  (case encoder
    "zip-json" {:status 200
                :body   (kafka-producer/send-message topic (->zip-json message))}
    {:status 400
     :body   (format "Unknown encoder %s, known are zip-json" encoder)}))
