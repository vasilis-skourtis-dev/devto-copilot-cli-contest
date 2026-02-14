/**
 * Valentine Cards - JavaScript for Card Operations
 * Handles drag-and-drop, form submissions, and API calls
 */

// ============================================
// Mode Switching
// ============================================

function switchMode(mode) {
    const readSection = document.getElementById('readSection');
    const createSection = document.getElementById('createSection');
    const readTab = document.getElementById('readTab');
    const createTab = document.getElementById('createTab');

    if (mode === 'read') {
        readSection.classList.add('active');
        createSection.classList.remove('active');
        readTab.classList.add('active');
        createTab.classList.remove('active');
        clearResults();
    } else {
        createSection.classList.add('active');
        readSection.classList.remove('active');
        createTab.classList.add('active');
        readTab.classList.remove('active');
        clearResults();
    }
}

// ============================================
// Drag and Drop for Read Section
// ============================================

const dropAreaRead = document.getElementById('dropAreaRead');
const readImageInput = document.getElementById('readImageInput');

// Prevent default drag behaviors
['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    dropAreaRead.addEventListener(eventName, preventDefaults, false);
    document.body.addEventListener(eventName, preventDefaults, false);
});

function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
}

// Highlight drop area when item is dragged over it
['dragenter', 'dragover'].forEach(eventName => {
    dropAreaRead.addEventListener(eventName, () => {
        dropAreaRead.classList.add('drag-over');
    }, false);
});

['dragleave', 'drop'].forEach(eventName => {
    dropAreaRead.addEventListener(eventName, () => {
        dropAreaRead.classList.remove('drag-over');
    }, false);
});

// Handle dropped files
dropAreaRead.addEventListener('drop', handleReadDrop, false);

function handleReadDrop(e) {
    const dt = e.dataTransfer;
    const files = dt.files;
    handleReadFiles(files);
}

// Handle file selection via input
readImageInput.addEventListener('change', function() {
    handleReadFiles(this.files);
});

function handleReadFiles(files) {
    if (files.length === 0) return;

    const file = files[0];
    
    if (!file.type.startsWith('image/')) {
        showError('readError', 'Please upload an image file');
        return;
    }

    readCard(file);
}

// ============================================
// Drag and Drop for Create Section
// ============================================

const dropAreaCreate = document.getElementById('dropAreaCreate');
const backgroundImageInput = document.getElementById('backgroundImageInput');
const imagePreview = document.getElementById('imagePreview');
const previewImg = document.getElementById('previewImg');

['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    dropAreaCreate.addEventListener(eventName, preventDefaults, false);
});

['dragenter', 'dragover'].forEach(eventName => {
    dropAreaCreate.addEventListener(eventName, () => {
        dropAreaCreate.classList.add('drag-over');
    }, false);
});

['dragleave', 'drop'].forEach(eventName => {
    dropAreaCreate.addEventListener(eventName, () => {
        dropAreaCreate.classList.remove('drag-over');
    }, false);
});

dropAreaCreate.addEventListener('drop', handleCreateDrop, false);

function handleCreateDrop(e) {
    const dt = e.dataTransfer;
    const files = dt.files;
    handleBackgroundImage(files);
}

backgroundImageInput.addEventListener('change', function() {
    handleBackgroundImage(this.files);
});

function handleBackgroundImage(files) {
    if (files.length === 0) return;

    const file = files[0];
    
    if (!file.type.startsWith('image/')) {
        showError('createError', 'Please upload an image file');
        return;
    }

    // Show preview
    const reader = new FileReader();
    reader.onload = function(e) {
        previewImg.src = e.target.result;
        imagePreview.style.display = 'block';
        dropAreaCreate.querySelector('.drop-area-content').style.display = 'none';
    };
    reader.readAsDataURL(file);

    // Store file in input
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(file);
    backgroundImageInput.files = dataTransfer.files;
}

function clearImage() {
    imagePreview.style.display = 'none';
    dropAreaCreate.querySelector('.drop-area-content').style.display = 'block';
    backgroundImageInput.value = '';
    previewImg.src = '';
}

// ============================================
// Character Counter
// ============================================

const secretMessage = document.getElementById('secretMessage');
const charCount = document.getElementById('charCount');

secretMessage.addEventListener('input', function() {
    charCount.textContent = this.value.length;
});

// ============================================
// Read Card API Call
// ============================================

async function readCard(file) {
    clearResults();
    
    const formData = new FormData();
    formData.append('image', file);

    try {
        dropAreaRead.classList.add('loading');

        const response = await fetch('/api/cards/read', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.messageFound && result.status === 'SUCCESS') {
            showReadSuccess(result.message);
        } else {
            showError('readError', result.errorMessage || 'No message found in this image');
        }

    } catch (error) {
        console.error('Error reading card:', error);
        showError('readError', 'Failed to read card. Please try again.');
    } finally {
        dropAreaRead.classList.remove('loading');
    }
}

function showReadSuccess(message) {
    const readResult = document.getElementById('readResult');
    const decodedMessage = document.getElementById('decodedMessage');
    
    decodedMessage.textContent = message;
    readResult.style.display = 'block';
}

// ============================================
// Create Card Form Submission
// ============================================

const createForm = document.getElementById('createForm');
const createBtn = document.getElementById('createBtn');

createForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    
    clearResults();

    const message = document.getElementById('secretMessage').value.trim();
    const recipientEmail = document.getElementById('recipientEmail').value.trim();
    const senderName = document.getElementById('senderName').value.trim();
    const emailSubject = document.getElementById('emailSubject').value.trim();
    const backgroundImage = backgroundImageInput.files[0];

    // Validation
    if (!message) {
        showError('createError', 'Please enter a secret message');
        return;
    }

    if (!recipientEmail) {
        showError('createError', 'Please enter recipient email');
        return;
    }

    if (!backgroundImage) {
        showError('createError', 'Please select a background image');
        return;
    }

    // Prepare form data
    const formData = new FormData();
    formData.append('message', message);
    formData.append('recipientEmail', recipientEmail);
    formData.append('senderName', senderName);
    formData.append('emailSubject', emailSubject);
    formData.append('backgroundImage', backgroundImage);

    try {
        createBtn.disabled = true;
        createBtn.textContent = '⏳ Creating...';
        createForm.classList.add('loading');

        const response = await fetch('/api/cards/create', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        if (result.status === 'SUCCESS') {
            showCreateSuccess(result);
        } else {
            showError('createError', result.message || 'Failed to create card');
        }

    } catch (error) {
        console.error('Error creating card:', error);
        showError('createError', 'Failed to create card. Please try again.');
    } finally {
        createBtn.disabled = false;
        createBtn.textContent = '💌 Create & Send Card';
        createForm.classList.remove('loading');
    }
});

function showCreateSuccess(result) {
    const createResult = document.getElementById('createResult');
    const createSuccessMessage = document.getElementById('createSuccessMessage');
    const createdImagePreview = document.getElementById('createdImagePreview');
    const createdImg = document.getElementById('createdImg');
    
    createSuccessMessage.textContent = result.message;
    
    if (result.imagePreview) {
        createdImg.src = 'data:image/png;base64,' + result.imagePreview;
        createdImagePreview.style.display = 'block';
    }
    
    createResult.style.display = 'block';

    // Reset form
    createForm.reset();
    clearImage();
    charCount.textContent = '0';
}

// ============================================
// Error Handling
// ============================================

function showError(elementId, message) {
    const errorBox = document.getElementById(elementId);
    const errorMessage = document.getElementById(elementId + 'Message');
    
    errorMessage.textContent = message;
    errorBox.style.display = 'flex';

    // Auto-hide after 5 seconds
    setTimeout(() => {
        errorBox.style.display = 'none';
    }, 5000);
}

// ============================================
// Clear Results
// ============================================

function clearResults() {
    document.getElementById('readResult').style.display = 'none';
    document.getElementById('readError').style.display = 'none';
    document.getElementById('createResult').style.display = 'none';
    document.getElementById('createError').style.display = 'none';
}

// ============================================
// Initialize
// ============================================

console.log('💌 Valentine Cards - Ready to spread love!');
