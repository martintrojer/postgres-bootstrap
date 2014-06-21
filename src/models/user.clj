(ns models.user
  (:require [models.helpers :refer [query]]
            [honeysql.core :as sql]))

(defn get-user-by-name [name]
  (when name
    (query (sql/format {:select [:*]
                        :from [:site_user]
                        :where [:= :name name]}))))
