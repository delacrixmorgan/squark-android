package com.delacrixmorgan.squark.common

import java.text.DecimalFormat

enum class NumberFormatTypes(val decimal: DecimalFormat) {
    HUNDREDTH(DecimalFormat("###,##0.00")),
    MILLIONTH(DecimalFormat("###,##0.00M")),
    BILLIONTH(DecimalFormat("###,##0.00B")),
    TRILLIONTH(DecimalFormat("###,##0.00T")),
    QUADRILLIONTH(DecimalFormat("###,##0.00Q"))
}