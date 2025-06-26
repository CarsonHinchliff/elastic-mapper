{
    "bool": {
        "must_not": [
            {
                "exists": {
                    "field": "#(params.field)"
                }
            }
        ]
    }
}