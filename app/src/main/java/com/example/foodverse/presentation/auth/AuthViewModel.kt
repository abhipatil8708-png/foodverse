package com.example.foodverse.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodverse.data.model.UserProfile
import com.example.foodverse.data.repository.DonationRepository
import com.example.foodverse.data.repository.LocalDonationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val organization: String = "",
    val isRegisterMode: Boolean = false,
    val selectedRole: String = "Food Donor",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)

class AuthViewModel(
    private val repository: DonationRepository = LocalDonationRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name, errorMessage = null)
    }

    fun onOrganizationChange(org: String) {
        _uiState.value = _uiState.value.copy(organization = org, errorMessage = null)
    }

    fun onRoleChange(role: String) {
        _uiState.value = _uiState.value.copy(selectedRole = role)
    }

    fun toggleAuthMode() {
        _uiState.value = _uiState.value.copy(
            isRegisterMode = !_uiState.value.isRegisterMode,
            errorMessage = null
        )
    }

    fun submitAuth() {
        val state = _uiState.value
        if (state.email.isBlank() || !state.email.contains("@")) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid email address.")
            return
        }
        if (state.password.length < 6) {
            _uiState.value = state.copy(errorMessage = "Password must be at least 6 characters.")
            return
        }
        if (state.isRegisterMode && state.name.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter your name.")
            return
        }

        _uiState.value = state.copy(isLoading = true)
        viewModelScope.launch {
            // Update repository profile
            val profile = UserProfile(
                name = if (state.name.isNotBlank()) state.name else "Alex Chen",
                email = state.email,
                role = state.selectedRole,
                organization = if (state.organization.isNotBlank()) state.organization else "Community Partner"
            )
            repository.updateProfile(profile)
            _uiState.value = _uiState.value.copy(isLoading = false, isAuthenticated = true)
        }
    }

    fun continueAsDemoGuest() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val demoProfile = UserProfile(
                name = "Alex Chen",
                email = "alex.chen@foodverse.org",
                role = "Community Donor & Volunteer",
                organization = "Green Hope Initiative",
                donationsCount = 14,
                mealsSaved = 186
            )
            repository.updateProfile(demoProfile)
            _uiState.value = _uiState.value.copy(isLoading = false, isAuthenticated = true)
        }
    }
}
