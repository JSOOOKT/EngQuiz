// app.js - Utility Functions for English Quiz Adventure

// ========================================
// CONFIGURATION
// ========================================

// Backend API URL - Change this if your backend runs on different port
const API_BASE_URL = 'http://localhost:8080';

// ========================================
// USER AUTHENTICATION HELPERS
// ========================================

/**
 * Get current logged in user from localStorage
 * @returns {Object|null} User object or null if not logged in
 */
function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    if (!userStr) return null;
    
    try {
        return JSON.parse(userStr);
    } catch (e) {
        console.error('Error parsing user data:', e);
        return null;
    }
}

/**
 * Save user data to localStorage
 * @param {Object} user - User object to save
 */
function saveUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

/**
 * Check if user is logged in, redirect to login if not
 * @returns {Object|null} User object or redirects to login
 */
function requireAuth() {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = 'login.html';
        return null;
    }
    return user;
}

/**
 * Logout user and redirect to login page
 */
function logout() {
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

// ========================================
// API CALL HELPERS
// ========================================

/**
 * Make API call with error handling
 * @param {string} endpoint - API endpoint (e.g., '/users')
 * @param {Object} options - Fetch options (method, body, etc.)
 * @returns {Promise<Object>} Response data
 */
async function apiCall(endpoint, options = {}) {
    try {
        const url = `${API_BASE_URL}${endpoint}`;
        
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
            },
        };
        
        const response = await fetch(url, { ...defaultOptions, ...options });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('API call error:', error);
        throw error;
    }
}

/**
 * Login user
 * @param {string} username
 * @param {string} password
 * @returns {Promise<Object>} Response with user data
 */
async function loginUser(username, password) {
    return await apiCall('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ username, password })
    });
}

/**
 * Register new user
 * @param {string} username
 * @param {string} email
 * @param {string} password
 * @returns {Promise<Object>} Response with user data
 */
async function registerUser(username, email, password) {
    return await apiCall('/auth/register', {
        method: 'POST',
        body: JSON.stringify({ username, email, password })
    });
}

/**
 * Get all quests
 * @returns {Promise<Array>} Array of quests
 */
async function getQuests() {
    return await apiCall('/quests');
}

/**
 * Get quizzes for a specific quest
 * @param {number} questId
 * @returns {Promise<Array>} Array of quizzes
 */
async function getQuizzesByQuest(questId) {
    return await apiCall(`/quests/${questId}/quizzes`);
}

/**
 * Get all questions
 * @returns {Promise<Array>} Array of questions
 */
async function getQuestions() {
    return await apiCall('/questions');
}

/**
 * Get leaderboard (all users sorted by score)
 * @returns {Promise<Array>} Array of users
 */
async function getLeaderboard() {
    const users = await apiCall('/users');
    return users.sort((a, b) => b.totalScore - a.totalScore);
}

// ========================================
// UI HELPERS
// ========================================

/**
 * Show alert message
 * @param {string} message - Message to show
 * @param {string} type - Alert type (success, danger, warning, info)
 * @param {string} elementId - ID of element to show alert in
 */
function showAlert(message, type = 'info', elementId = 'alertBox') {
    const alertBox = document.getElementById(elementId);
    if (!alertBox) return;
    
    alertBox.className = `alert alert-${type}`;
    alertBox.textContent = message;
    alertBox.classList.remove('d-none');
}

/**
 * Hide alert message
 * @param {string} elementId - ID of element to hide
 */
function hideAlert(elementId = 'alertBox') {
    const alertBox = document.getElementById(elementId);
    if (!alertBox) return;
    
    alertBox.classList.add('d-none');
}

/**
 * Show loading spinner
 * @param {string} elementId - ID of element to show spinner in
 */
function showLoading(elementId) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    element.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2 text-muted">Loading...</p>
        </div>
    `;
}

/**
 * Format date to readable string
 * @param {string|Date} date
 * @returns {string} Formatted date
 */
function formatDate(date) {
    const d = new Date(date);
    return d.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

/**
 * Get difficulty color for badges
 * @param {string} difficulty - Easy, Medium, or Hard
 * @returns {string} Bootstrap color class
 */
function getDifficultyColor(difficulty) {
    const colors = {
        'Easy': 'success',
        'Medium': 'warning',
        'Hard': 'danger'
    };
    return colors[difficulty] || 'primary';
}

// ========================================
// FORM VALIDATION HELPERS
// ========================================

/**
 * Validate email format
 * @param {string} email
 * @returns {boolean} True if valid
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate password strength
 * @param {string} password
 * @returns {Object} { valid: boolean, message: string }
 */
function validatePassword(password) {
    if (password.length < 6) {
        return { valid: false, message: 'Password must be at least 6 characters' };
    }
    return { valid: true, message: 'Password is valid' };
}

/**
 * Validate username
 * @param {string} username
 * @returns {Object} { valid: boolean, message: string }
 */
function validateUsername(username) {
    if (username.length < 3) {
        return { valid: false, message: 'Username must be at least 3 characters' };
    }
    if (!/^[a-zA-Z0-9_]+$/.test(username)) {
        return { valid: false, message: 'Username can only contain letters, numbers, and underscores' };
    }
    return { valid: true, message: 'Username is valid' };
}

// ========================================
// LOCAL STORAGE HELPERS
// ========================================

/**
 * Save data to localStorage
 * @param {string} key
 * @param {any} value
 */
function saveToStorage(key, value) {
    try {
        localStorage.setItem(key, JSON.stringify(value));
    } catch (e) {
        console.error('Error saving to localStorage:', e);
    }
}

/**
 * Get data from localStorage
 * @param {string} key
 * @returns {any} Parsed data or null
 */
function getFromStorage(key) {
    try {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    } catch (e) {
        console.error('Error reading from localStorage:', e);
        return null;
    }
}

/**
 * Remove data from localStorage
 * @param {string} key
 */
function removeFromStorage(key) {
    localStorage.removeItem(key);
}

// ========================================
// DEBUG HELPERS (for development)
// ========================================

/**
 * Log to console only in development
 * @param  {...any} args
 */
function debugLog(...args) {
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
        console.log('[DEBUG]', ...args);
    }
}

/**
 * Check if backend is reachable
 * @returns {Promise<boolean>}
 */
async function checkBackendHealth() {
    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        return response.ok;
    } catch (e) {
        return false;
    }
}

// ========================================
// INITIALIZE ON PAGE LOAD
// ========================================

document.addEventListener('DOMContentLoaded', () => {
    debugLog('App initialized');
    
    // Check backend connection (only on localhost)
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
        checkBackendHealth().then(isHealthy => {
            if (!isHealthy) {
                console.warn('⚠️ Backend appears to be offline. Make sure Spring Boot is running on port 8080');
            } else {
                debugLog('✅ Backend is reachable');
            }
        });
    }
});

// ========================================
// EXPORT FOR GLOBAL USE
// ========================================

// All functions are now available globally
// You can call them from any HTML page like:
// getCurrentUser(), loginUser(), showAlert(), etc.

console.log('✅ app.js loaded successfully');