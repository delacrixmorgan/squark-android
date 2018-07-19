package com.delacrixmorgan.squark.common

import java.text.DecimalFormat

/**
 * NumberFormatTypes
 * squark-android
 *
 * Created by Delacrix Morgan on 18/07/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

enum class NumberFormatTypes(val decimal: DecimalFormat) {
    HUNDREDTH(DecimalFormat("###,##0.00")),
    THOUSANDTH(DecimalFormat("###,##0.00K")),
    MILLIONTH(DecimalFormat("###,##0.00M")),
    BILLIONTH(DecimalFormat("###,##0.00B")),
    TRILLIONTH(DecimalFormat("###,##0.00T")),
    QUADRILLIONTH(DecimalFormat("###,##0.00Q"))
}

