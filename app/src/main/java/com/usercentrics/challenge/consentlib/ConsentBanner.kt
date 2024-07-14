package com.usercentrics.challenge.consentlib

import android.content.Context
import android.util.Log
import com.usercentrics.challenge.BuildConfig
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsBanner
import com.usercentrics.sdk.UsercentricsConsentUserResponse
import com.usercentrics.sdk.UsercentricsOptions
import com.usercentrics.sdk.v2.settings.data.UsercentricsService

class ConsentBanner {
    companion object {
        fun initializeUsercentricsSDK(context: Context) {
            val options = UsercentricsOptions(settingsId = BuildConfig.SETTINGS_ID)
            Usercentrics.initialize(context, options)
        }

        fun showConsentBanner(context: Context, cb: (Int) -> Unit) {
            Usercentrics.isReady({ status ->
                if (status.shouldCollectConsent) {
                    // Show banner to collect consent
                    collectConsent(context = context, cb = cb)
                } else {
                    // Apply consent with status.consents
                    // Should be "applyConsent(status.consents)", but this is for QOL so we can press
                    // the banner button multiple times to check the result
                    collectConsent(context = context, cb = cb)
                }
            }, { error ->
                // Handle non-localized error
            })
        }

        private fun collectConsent(context: Context, cb: (Int) -> Unit) {
            val banner = UsercentricsBanner(context = context)
            banner.showSecondLayer { userResponse ->
                checkConsent(userResponse = userResponse, cb = cb)
            }
        }

        private fun checkConsent(
            userResponse: UsercentricsConsentUserResponse?,
            cb: (Int) -> Unit
        ) {
            val cmpData = Usercentrics.instance.getCMPData()
            val consentedServicesInfo = mutableListOf<UsercentricsService>()
            userResponse?.consents?.forEach { userResponseConsent ->
                val service = cmpData.services.find { cmpDataService ->
                    userResponseConsent.templateId == cmpDataService.templateId && userResponseConsent.status
                }
                service?.let { consentedServicesInfo.add(it) }
            }
            calculateCost(consentedServicesInfo = consentedServicesInfo, cb = cb)
        }

        private fun calculateCost(
            consentedServicesInfo: List<UsercentricsService>,
            cb: (Int) -> Unit
        ) {
            var totalCost = 0
            consentedServicesInfo.forEach { serviceInfo ->
                val name = serviceInfo.dataProcessor
                var cost = 0
                serviceInfo.dataCollectedList.forEach {
                    cost += checkCost(it)
                }
                cost =
                    specialCostRules(cost = cost, dataCollectedList = serviceInfo.dataCollectedList)
                Log.i("Consented Service", "$name: $cost")
                totalCost += cost
            }
            cb.invoke(totalCost)
        }

        private fun checkCost(collectedData: String): Int {
            return when (collectedData.lowercase()) {
                "configuration of app settings" -> 1
                "ip address" -> 2
                "user behaviour" -> 2
                "user agent" -> 3
                "app crashes" -> -2
                "browser information" -> 3
                "credit and debit card number" -> 4
                "first name" -> 6
                "geographic location" -> 7
                "date and time of visit" -> 1
                "advertising identifier" -> 2
                "bank details" -> 5
                "purchase activity" -> 6
                "internet service provider" -> 4
                "javascript support" -> -1
                else -> 0
            }
        }

        private fun specialCostRules(cost: Int, dataCollectedList: List<String>): Int {
            var newCost = cost

            // Rule 1
            // If a service declares: Purchase activity, Bank details AND Credit and debit card number:
            // Increase the cost by 10%.
            if (ruleChecker(
                    dataCollectedList = dataCollectedList,
                    dataCollected = arrayOf(
                        "purchase activity", "bank details", "credit and debit card"
                    )
                )
            ) {
                newCost += Math.round(newCost * 0.10).toInt()
            }

            // Rule 2
            // If a service declares: Search terms, Geographic location AND IP Address.
            // Increase the cost by 27%.
            if (ruleChecker(
                    dataCollectedList = dataCollectedList,
                    dataCollected = arrayOf(
                        "search terms", "geographic location", "ip address"
                    )
                )
            ) {
                newCost += Math.round(newCost * 0.27).toInt()
            }

            // Rule 3
            // If a service declares 4 or less "data types".
            // Decrease the cost by 10%
            if (dataCollectedList.size <= 4) {
                newCost -= Math.round(newCost * 0.10).toInt()
            }
            return newCost
        }
        

        private fun ruleChecker(
            dataCollectedList: List<String>, vararg dataCollected: String
        ): Boolean {
            dataCollected.forEach { _ ->
                if (!dataCollectedList.any { it.equals(other = it, ignoreCase = true) }) {
                    return false
                }
            }
            return true
        }
    }
}