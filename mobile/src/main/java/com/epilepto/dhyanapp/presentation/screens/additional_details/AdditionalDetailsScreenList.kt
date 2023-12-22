package com.epilepto.dhyanapp.presentation.screens.additional_details

sealed interface AdditionalDetailsScreenList {
    data object Gender: AdditionalDetailsScreenList
     data object Age: AdditionalDetailsScreenList
    data object Goal: AdditionalDetailsScreenList
}