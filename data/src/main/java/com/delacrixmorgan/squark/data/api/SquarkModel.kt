package com.delacrixmorgan.squark.data.api

import com.google.gson.annotations.SerializedName

/**
 * SquarkModel
 * squark-android
 *
 * Created by Delacrix Morgan on 15/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

object SquarkModel {
    data class Result(
            @field:SerializedName("terms")
            val terms: String? = null,

            @field:SerializedName("success")
            val success: Boolean? = null,

            @field:SerializedName("privacy")
            val privacy: String? = null,

            @field:SerializedName("source")
            val source: String? = null,

            @field:SerializedName("timestamp")
            val timestamp: Int? = null,

            @field:SerializedName("quotes")
            val quotes: Quotes? = null
    )

    data class Quotes(
            @field:SerializedName("USDBIF")
            val usdBIF: Double? = null,

            @field:SerializedName("USDMWK")
            val usdMWK: Double? = null,

            @field:SerializedName("USDBYR")
            val usdBYR: Double? = null,

            @field:SerializedName("USDBYN")
            val usdBYN: Double? = null,

            @field:SerializedName("USDHUF")
            val usdHUF: Double? = null,

            @field:SerializedName("USDAOA")
            val usdAOA: Double? = null,

            @field:SerializedName("USDJPY")
            val usdJPY: Double? = null,

            @field:SerializedName("USDMNT")
            val usdMNT: Double? = null,

            @field:SerializedName("USDPLN")
            val usdPLN: Double? = null,

            @field:SerializedName("USDGBP")
            val usdGBP: Double? = null,

            @field:SerializedName("USDSBD")
            val usdSBD: Double? = null,

            @field:SerializedName("USDAWG")
            val usdAWG: Double? = null,

            @field:SerializedName("USDKRW")
            val usdKRW: Double? = null,

            @field:SerializedName("USDNPR")
            val usdNPR: Double? = null,

            @field:SerializedName("USDINR")
            val usdINR: Double? = null,

            @field:SerializedName("USDYER")
            val usdYER: Double? = null,

            @field:SerializedName("USDAFN")
            val usdAFN: Double? = null,

            @field:SerializedName("USDMVR")
            val usdMVR: Double? = null,

            @field:SerializedName("USDKZT")
            val usdKZT: Double? = null,

            @field:SerializedName("USDSRD")
            val usdSRD: Double? = null,

            @field:SerializedName("USDSZL")
            val usdSZL: Double? = null,

            @field:SerializedName("USDLTL")
            val usdLTL: Double? = null,

            @field:SerializedName("USDSAR")
            val usdSAR: Double? = null,

            @field:SerializedName("USDTTD")
            val usdTTD: Double? = null,

            @field:SerializedName("USDBHD")
            val usdBHD: Double? = null,

            @field:SerializedName("USDHTG")
            val usdHTG: Double? = null,

            @field:SerializedName("USDANG")
            val usdANG: Double? = null,

            @field:SerializedName("USDPKR")
            val usdPKR: Double? = null,

            @field:SerializedName("USDXCD")
            val usdXCD: Double? = null,

            @field:SerializedName("USDLKR")
            val usdLKR: Double? = null,

            @field:SerializedName("USDNGN")
            val usdNGN: Double? = null,

            @field:SerializedName("USDCRC")
            val usdCRC: Double? = null,

            @field:SerializedName("USDCZK")
            val usdCZK: Double? = null,

            @field:SerializedName("USDZWL")
            val usdZWL: Double? = null,

            @field:SerializedName("USDGIP")
            val usdGIP: Double? = null,

            @field:SerializedName("USDRON")
            val usdRON: Double? = null,

            @field:SerializedName("USDMMK")
            val usdMMK: Double? = null,

            @field:SerializedName("USDMUR")
            val usdMUR: Double? = null,

            @field:SerializedName("USDNOK")
            val usdNOK: Double? = null,

            @field:SerializedName("USDSYP")
            val usdSYP: Double? = null,

            @field:SerializedName("USDIMP")
            val usdIMP: Double? = null,

            @field:SerializedName("USDCAD")
            val usdCAD: Double? = null,

            @field:SerializedName("USDBGN")
            val usdBGN: Double? = null,

            @field:SerializedName("USDRSD")
            val usdRSD: Double? = null,

            @field:SerializedName("USDDOP")
            val usdDOP: Double? = null,

            @field:SerializedName("USDKMF")
            val usdKMF: Double? = null,

            @field:SerializedName("USDCUP")
            val usdCUP: Double? = null,

            @field:SerializedName("USDGMD")
            val usdGMD: Double? = null,

            @field:SerializedName("USDTWD")
            val usdTWD: Double? = null,

            @field:SerializedName("USDIQD")
            val usdIQD: Double? = null,

            @field:SerializedName("USDSDG")
            val usdSDG: Double? = null,

            @field:SerializedName("USDBSD")
            val usdBSD: Double? = null,

            @field:SerializedName("USDSLL")
            val usdSLL: Double? = null,

            @field:SerializedName("USDCUC")
            val usdCUC: Double? = null,

            @field:SerializedName("USDZAR")
            val usdZAR: Double? = null,

            @field:SerializedName("USDTND")
            val usdTND: Double? = null,

            @field:SerializedName("USDCLP")
            val usdCLP: Double? = null,

            @field:SerializedName("USDHNL")
            val usdHNL: Double? = null,

            @field:SerializedName("USDUGX")
            val usdUGX: Double? = null,

            @field:SerializedName("USDMXN")
            val usdMXN: Double? = null,

            @field:SerializedName("USDSTD")
            val usdSTD: Double? = null,

            @field:SerializedName("USDLVL")
            val usdLVL: Double? = null,

            @field:SerializedName("USDSCR")
            val usdSCR: Double? = null,

            @field:SerializedName("USDCDF")
            val usdCDF: Double? = null,

            @field:SerializedName("USDBBD")
            val usdBBD: Double? = null,

            @field:SerializedName("USDGTQ")
            val usdGTQ: Double? = null,

            @field:SerializedName("USDFJD")
            val usdFJD: Double? = null,

            @field:SerializedName("USDTMT")
            val usdTMT: Double? = null,

            @field:SerializedName("USDCLF")
            val usdCLF: Double? = null,

            @field:SerializedName("USDBRL")
            val usdBRL: Double? = null,

            @field:SerializedName("USDPEN")
            val usdPEN: Double? = null,

            @field:SerializedName("USDNZD")
            val usdNZD: Double? = null,

            @field:SerializedName("USDWST")
            val usdWST: Double? = null,

            @field:SerializedName("USDNIO")
            val usdNIO: Double? = null,

            @field:SerializedName("USDBAM")
            val usdBAM: Double? = null,

            @field:SerializedName("USDEGP")
            val usdEGP: Double? = null,

            @field:SerializedName("USDMOP")
            val usdMOP: Double? = null,

            @field:SerializedName("USDNAD")
            val usdNAD: Double? = null,

            @field:SerializedName("USDBZD")
            val usdBZD: Double? = null,

            @field:SerializedName("USDMGA")
            val usdMGA: Double? = null,

            @field:SerializedName("USDXDR")
            val usdXDR: Double? = null,

            @field:SerializedName("USDCOP")
            val usdCOP: Double? = null,

            @field:SerializedName("USDRUB")
            val usdRUB: Double? = null,

            @field:SerializedName("USDPYG")
            val usdPYG: Double? = null,

            @field:SerializedName("USDISK")
            val usdISK: Double? = null,

            @field:SerializedName("USDJMD")
            val usdJMD: Double? = null,

            @field:SerializedName("USDLYD")
            val usdLYD: Double? = null,

            @field:SerializedName("USDBMD")
            val usdBMD: Double? = null,

            @field:SerializedName("USDKWD")
            val usdKWD: Double? = null,

            @field:SerializedName("USDPHP")
            val usdPHP: Double? = null,

            @field:SerializedName("USDBDT")
            val usdBDT: Double? = null,

            @field:SerializedName("USDCNY")
            val usdCNY: Double? = null,

            @field:SerializedName("USDTHB")
            val usdTHB: Double? = null,

            @field:SerializedName("USDUZS")
            val usdUZS: Double? = null,

            @field:SerializedName("USDXPF")
            val usdXPF: Double? = null,

            @field:SerializedName("USDMRO")
            val usdMRO: Double? = null,

            @field:SerializedName("USDIRR")
            val usdIRR: Double? = null,

            @field:SerializedName("USDARS")
            val usdARS: Double? = null,

            @field:SerializedName("USDQAR")
            val usdQAR: Double? = null,

            @field:SerializedName("USDGNF")
            val usdGNF: Double? = null,

            @field:SerializedName("USDERN")
            val usdERN: Double? = null,

            @field:SerializedName("USDMZN")
            val usdMZN: Double? = null,

            @field:SerializedName("USDSVC")
            val usdSVC: Double? = null,

            @field:SerializedName("USDBTN")
            val usdBTN: Double? = null,

            @field:SerializedName("USDUAH")
            val usdUAH: Double? = null,

            @field:SerializedName("USDKES")
            val usdKES: Double? = null,

            @field:SerializedName("USDSEK")
            val usdSEK: Double? = null,

            @field:SerializedName("USDCVE")
            val usdCVE: Double? = null,

            @field:SerializedName("USDAZN")
            val usdAZN: Double? = null,

            @field:SerializedName("USDTOP")
            val usdTOP: Double? = null,

            @field:SerializedName("USDOMR")
            val usdOMR: Double? = null,

            @field:SerializedName("USDPGK")
            val usdPGK: Double? = null,

            @field:SerializedName("USDXOF")
            val usdXOF: Double? = null,

            @field:SerializedName("USDGEL")
            val usdGEL: Double? = null,

            @field:SerializedName("USDBTC")
            val usdBTC: Double? = null,

            @field:SerializedName("USDUYU")
            val usdUYU: Double? = null,

            @field:SerializedName("USDMAD")
            val usdMAD: Double? = null,

            @field:SerializedName("USDFKP")
            val usdFKP: Double? = null,

            @field:SerializedName("USDMYR")
            val usdMYR: Double? = null,

            @field:SerializedName("USDEUR")
            val usdEUR: Double? = null,

            @field:SerializedName("USDLSL")
            val usdLSL: Double? = null,

            @field:SerializedName("USDDKK")
            val usdDKK: Double? = null,

            @field:SerializedName("USDJOD")
            val usdJOD: Double? = null,

            @field:SerializedName("USDHKD")
            val usdHKD: Double? = null,

            @field:SerializedName("USDRWF")
            val usdRWF: Double? = null,

            @field:SerializedName("USDAED")
            val usdAED: Double? = null,

            @field:SerializedName("USDBWP")
            val usdBWP: Double? = null,

            @field:SerializedName("USDSHP")
            val usdSHP: Double? = null,

            @field:SerializedName("USDTRY")
            val usdTRY: Double? = null,

            @field:SerializedName("USDLBP")
            val usdLBP: Double? = null,

            @field:SerializedName("USDTJS")
            val usdTJS: Double? = null,

            @field:SerializedName("USDIDR")
            val usdIDR: Double? = null,

            @field:SerializedName("USDKYD")
            val usdKYD: Double? = null,

            @field:SerializedName("USDAMD")
            val usdAMD: Double? = null,

            @field:SerializedName("USDGHS")
            val usdGHS: Double? = null,

            @field:SerializedName("USDGYD")
            val usdGYD: Double? = null,

            @field:SerializedName("USDKPW")
            val usdKPW: Double? = null,

            @field:SerializedName("USDBOB")
            val usdBOB: Double? = null,

            @field:SerializedName("USDKHR")
            val usdKHR: Double? = null,

            @field:SerializedName("USDMDL")
            val usdMDL: Double? = null,

            @field:SerializedName("USDAUD")
            val usdAUD: Double? = null,

            @field:SerializedName("USDILS")
            val usdILS: Double? = null,

            @field:SerializedName("USDTZS")
            val usdTZS: Double? = null,

            @field:SerializedName("USDVND")
            val usdVND: Double? = null,

            @field:SerializedName("USDXAU")
            val usdXAU: Double? = null,

            @field:SerializedName("USDZMW")
            val usdZMW: Double? = null,

            @field:SerializedName("USDLRD")
            val usdLRD: Double? = null,

            @field:SerializedName("USDXAG")
            val usdXAG: Double? = null,

            @field:SerializedName("USDALL")
            val usdALL: Double? = null,

            @field:SerializedName("USDCHF")
            val usdCHF: Double? = null,

            @field:SerializedName("USDHRK")
            val usdHRK: Double? = null,

            @field:SerializedName("USDDJF")
            val usdDJF: Double? = null,

            @field:SerializedName("USDXAF")
            val usdXAF: Double? = null,

            @field:SerializedName("USDKGS")
            val usdKGS: Double? = null,

            @field:SerializedName("USDSOS")
            val usdSOS: Double? = null,

            @field:SerializedName("USDVEF")
            val usdVEF: Double? = null,

            @field:SerializedName("USDVUV")
            val usdVUV: Double? = null,

            @field:SerializedName("USDLAK")
            val usdLAK: Double? = null,

            @field:SerializedName("USDBND")
            val usdBND: Double? = null,

            @field:SerializedName("USDZMK")
            val usdZMK: Double? = null,

            @field:SerializedName("USDETB")
            val usdETB: Double? = null,

            @field:SerializedName("USDJEP")
            val usdJEP: Double? = null,

            @field:SerializedName("USDDZD")
            val usdDZD: Double? = null,

            @field:SerializedName("USDPAB")
            val usdPAB: Double? = null,

            @field:SerializedName("USDGGP")
            val usdGGP: Double? = null,

            @field:SerializedName("USDSGD")
            val usdSGD: Double? = null,

            @field:SerializedName("USDMKD")
            val usdMKD: Double? = null,

            @field:SerializedName("USDUSD")
            val usdUSD: Double? = null
    )
}

