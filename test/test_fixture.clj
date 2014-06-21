(ns test-fixture
  (:require [clojure.java.jdbc :as jdbc]
            [connection :refer [db-connection]]
            [honeysql.helpers :as h]
            [honeysql.core :as sql]))

(defn- truncate-tables []
  (jdbc/db-do-commands (db-connection) "TRUNCATE site_user_branch, site_user, branch, company"))

(defn with-fresh-db [seed-fn]
  (fn [f]
    (truncate-tables)
    (seed-fn (db-connection))
    (f)))

(defn seed-data [db-spec]

  (jdbc/execute! db-spec
                 (sql/format {:insert-into :company
                              :columns [:name]
                              :values [["foxton"]]}))
  (jdbc/execute! db-spec
                 (sql/format {:insert-into :branch
                              :columns [:name :branch_id]
                              :values [["brixton" "1234"]
                                       ["southwark" "5678"]]}))
    (jdbc/execute! db-spec
                 (sql/format {:insert-into :site_user
                              :columns [:name :password]
                              :values [["agent1" "mutual"]    ;; TODO -- store password hashes
                                       ["agent2" "mutual"]]}))

  (let [[company] (jdbc/query db-spec
                              (sql/format {:select [:id] :from [:company]}))
        [brixton southwark] (jdbc/query db-spec
                                        (sql/format {:select [:id]
                                                     :from [:branch]
                                                     :order-by [:name]}))
        [agent1 agent2] (jdbc/query db-spec
                                    (sql/format {:select [:id]
                                                 :from [:site_user]
                                                 :order-by [:name]}))]

    (jdbc/execute! db-spec
                   (sql/format {:update :branch :set {:company (:id company)}}))

    (jdbc/execute! db-spec
                   (sql/format {:update :site_user :set {:company (:id company)}}))

    (jdbc/execute! db-spec
                   (sql/format {:insert-into :site_user_branch
                                :columns [:site_user :branch]
                                :values [[(:id agent1) (:id brixton)]
                                         [(:id agent2) (:id southwark)]]}))))
