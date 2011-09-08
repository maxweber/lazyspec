(ns lazyspec.core
  (:use lazytest.suite
        lazytest.test-case
        lazyspec.doc))

(defn- clasp [name f]
  (fn [doc & fns]
    (suite (fn []
             (test-seq
              (with-meta
                (map test-case (f fns))
                {:doc (str name ": " doc)}))))))

(def Scenario (clasp "Scenario" flatten))

(def Feature (clasp "Feature" flatten))

(defn- block [name]
  (fn [& fns]
    (let [fns (map add-fn-doc fns)
          [first & rest] fns
          first (change-doc first #(str name " " %))
          rest (map (fn [f] (change-doc f #(str "And " %))) rest)
          fns (cons first rest)]
      fns)))

(def Given (block "Given"))

(def When (block "When"))

(def Then (block "Then"))

(defn capture [context fun f]
  (with-meta (fn []
               (let [r (f)
                     bindings (fun r)]
                 (apply swap! context assoc (apply concat bindings))
                 r))
    (meta f)))

(defn with-context [context f & args]
  (with-doc (fn []
              (apply f (map #(do
                               (if (keyword? %)
                                 (if-let [val (% @context)]
                                   val
                                   %)
                                 %)) args)))
    (resolve-fn-doc f)))
