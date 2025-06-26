{
    "bool": {
        "must_not": [
            {
                "term": {
                    "#(params.field)": {
                        "value": "#(params.value)"
                    }
                }
            }
        ]
    }
}