(ns test-producer.kafka.producer
  (:require [clj-kafka-x.producer :as kp]
            [mount.core :as mount]))

(mount/defstate instance
  :start (kp/producer {"group.id"          (:group-id (mount/args))
                       "bootstrap.servers" (:bootstrap-servers (mount/args))}
                      (kp/byte-array-serializer)
                      (kp/byte-array-serializer))
  :stop (.close instance))

(defn send-message [topic msg]
  @(kp/send instance (kp/record topic msg)))
