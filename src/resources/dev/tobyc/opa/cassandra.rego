package cassandra

default result = ["SELECT", "MODIFY", "CREATE"]


permissions := {
    "modify_schema": ["CREATE", "ALTER", "DROP"],
    "select": ["SELECT"],
    "update": ["MODIFY"],
    "execute": ["EXECUTE"]
}
