(ns test-producer.api.routes
  (:require [test-producer.api.handlers.topic :as topic]))


(def routes
  [["/:encoder/:topic" {:post    topic/post
                        :summary "Encodes and posts message"}]])
