java_library(
    name = "string-formatter",
    srcs = glob(["idwall/desafio/string/*.java"]),
    visibility = ["//src/test/java:__pkg__"]
)

java_binary(
    name = "Main",
    srcs = ["idwall/desafio/Main.java"],
    main_class = "idwall.desafio.Main",
    deps = [":string-formatter"]
)