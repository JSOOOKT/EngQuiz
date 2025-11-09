// ========================================
// CONFIGURATION
// ========================================
const API_BASE_URL = window.location.hostname.includes('github.dev')
    ? 'https://super-tribble-r4r5vg64vpwrh69g-8080.app.github.dev'
    : 'http://localhost:8080';

async function loginUser(username, password) {
    return await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    })
    .then(res => res.json()); // Parse JSON
}

function getCurrentUser() {
    const userStr = localStorage.getItem('user');
    if (!userStr) return null;
    try { return JSON.parse(userStr); } 
    catch (e) { console.error('Error parsing user data:', e); return null; }
}

function saveUser(user) {
    localStorage.setItem('user', JSON.stringify(user));
}

function requireAuth() {
    const user = getCurrentUser();
    if (!user) { window.location.href = 'login.html'; return null; }
    return user;
}

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

// ========================================
// API CALL HELPERS
// ========================================
async function apiCall(endpoint, options = {}) {
    try {
        const url = `${API_BASE_URL}${endpoint}`;
        const defaultOptions = { headers: { 'Content-Type': 'application/json' } };
        const response = await fetch(url, { ...defaultOptions, ...options });
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP ${response.status}: ${errorText}`);
        }
        return await response.json();
    } catch (error) {
        console.error('API call error:', error);
        throw error;
    }
}

async function loginUser(username, password) {
    return await apiCall('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) });
}

async function registerUser(username, email, password) {
    return await apiCall('/auth/register', { method: 'POST', body: JSON.stringify({ username, email, password }) });
}

async function getQuests() { return await apiCall('/quests'); }
async function getQuizzesByQuest(questId) { return await apiCall(`/quests/${questId}/quizzes`); }
async function getQuestions() { return await apiCall('/questions'); }
async function getLeaderboard() {
    const users = await apiCall('/users');
    return users.sort((a, b) => b.totalScore - a.totalScore);
}

// ========================================
// SAVE QUIZ SCORE TO BACKEND
// ========================================
async function saveUserScore(userId, newScore) {
    try {
        const user = await apiCall(`/users/${userId}`);
        const updatedScore = (user.totalScore || 0) + newScore;
        await apiCall(`/users/${userId}`, {
            method: 'PUT',
            body: JSON.stringify({ ...user, totalScore: updatedScore })
        });

        // Update localStorage
        saveUser({ ...user, totalScore: updatedScore });
    } catch (error) {
        console.error('Failed to save user score:', error);
    }
}

// ========================================
// UI HELPERS
// ========================================
function showAlert(message, type = 'info', elementId = 'alertBox') {
    const alertBox = document.getElementById(elementId);
    if (!alertBox) return;
    alertBox.className = `alert alert-${type}`;
    alertBox.textContent = message;
    alertBox.classList.remove('d-none');
}

function hideAlert(elementId = 'alertBox') {
    const alertBox = document.getElementById(elementId);
    if (!alertBox) return;
    alertBox.classList.add('d-none');
}

function formatDate(date) {
    const d = new Date(date);
    return d.toLocaleDateString('en-US', { year:'numeric', month:'short', day:'numeric', hour:'2-digit', minute:'2-digit' });
}

function getDifficultyColor(difficulty) {
    const colors = { 'Easy': 'success', 'Medium': 'warning', 'Hard': 'danger' };
    return colors[difficulty] || 'primary';
}

// ========================================
// LOCAL STORAGE HELPERS
// ========================================
function saveToStorage(key, value) {
    try { localStorage.setItem(key, JSON.stringify(value)); } 
    catch (e) { console.error('Error saving to localStorage:', e); }
}

function getFromStorage(key) {
    try {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    } catch (e) { console.error('Error reading from localStorage:', e); return null; }
}

function removeFromStorage(key) { localStorage.removeItem(key); }

// ========================================
// DEBUG
// ========================================
function debugLog(...args) {
    if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') console.log('[DEBUG]', ...args);
}

// ========================================
// INITIALIZE
// ========================================
document.addEventListener('DOMContentLoaded', () => {
    debugLog('App initialized');

    // Optional: Check backend health
    fetch(`${API_BASE_URL}/users`)
        .then(res => {
            if (!res.ok) throw new Error('Backend not reachable');
            debugLog('✅ Backend reachable');
        })
        .catch(err => {
            console.warn('⚠️ Backend might not be running:', err.message);
        });
});
