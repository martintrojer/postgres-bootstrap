(ns models.helpers
  (:require [clojure.java.jdbc :as jdbc]
            [connection :refer [db-connection]]))

(defn query [q]
  (jdbc/query (db-connection) q))
