(ns models.user-test
  (:require [clojure.test :refer :all]
            [models.user :refer :all]
            [test-fixture :refer [with-fresh-db seed-data]]))

(use-fixtures :each (with-fresh-db seed-data))

(deftest get-user-by-name-test
  (is (= 1 (count (get-user-by-name "agent1"))))
  (is (= 1 (count (get-user-by-name "agent2"))))
  (is (= 0 (count (get-user-by-name "agent3"))))
  (is (= 0 (count (get-user-by-name ""))))
  (is (= 0 (count (get-user-by-name nil)))))
