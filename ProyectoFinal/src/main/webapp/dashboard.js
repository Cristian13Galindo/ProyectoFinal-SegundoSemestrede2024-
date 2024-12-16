document.addEventListener('DOMContentLoaded', function () {
    loadCategories();
    loadBudgets();
    loadTransactionFilterBudgets(); // Cargar presupuestos en el filtro
    setupEventListeners();
});


// Mostrar las transacciones en el contenedor correspondiente
function displayTransactions(transactions) {
    const container = document.getElementById('transactionsContainer');
    if (!container) return;

    container.innerHTML = ''; // Limpiar contenido anterior

    if (transactions.length === 0) {
        container.innerHTML = '<p class="text-center">No hay transacciones registradas.</p>';
        return;
    }

    transactions.forEach(transaction => {
        const date = transaction.date ? formatDate(transaction.date) : 'Fecha no disponible';
        const html = `
            <div class="col-md-4">
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Presupuesto: ${transaction.budgetId}</h5>
                        <p><strong>Categoría:</strong> ${transaction.categoryName || 'Desconocida'}</p>
                        <p><strong>Monto:</strong> $${transaction.amount.toFixed(2)}</p>
                        <p><strong>Descripción:</strong> ${transaction.description || 'Sin descripción'}</p>
                        <p><strong>Fecha:</strong> ${date}</p>
                    </div>
                </div>
            </div>
        `;
        container.innerHTML += html;
    });
}

// Cargar categorías en los formularios
function loadCategories() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/ProyectoFinal_war_exploded/categories', true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    const categories = JSON.parse(xhr.responseText);
                    populateCategorySelect(categories);
                } catch (e) {
                    console.error('Error al parsear las categorías:', e);
                    alert('Error al cargar las categorías.');
                }
            } else {
                alert('Error al cargar las categorías: ' + xhr.status + ' - ' + xhr.statusText);
            }
        }
    };

    xhr.send();
}

// Llenar los selects de categorías en formularios
function populateCategorySelect(categories) {
    const categorySelect = document.getElementById('categorySelect');

    const optionsHtml = categories.map(category => {
        return `<option value="${category.categoryId}">${category.name}</option>`;
    }).join('');

    if (categorySelect) {
        categorySelect.innerHTML = '<option value="" disabled selected>Selecciona una categoría</option>' + optionsHtml;
    }
}

// Función para añadir una categoría al contenedor
function addCategory() {
    const categorySelect = document.getElementById('categorySelect');
    const allocatedAmountInput = document.getElementById('allocatedAmount');
    const thresholdInput = document.getElementById('threshold');
    const categoriesContainer = document.getElementById('categoriesContainer').querySelector('ul');

    const categoryId = categorySelect.value;
    const categoryName = categorySelect.options[categorySelect.selectedIndex]?.textContent;
    const allocatedAmount = parseFloat(allocatedAmountInput.value);
    const threshold = parseFloat(thresholdInput.value);

    // Validar entrada
    if (!categoryId || isNaN(allocatedAmount) || allocatedAmount <= 0 || isNaN(threshold) || threshold <= 0) {
        alert('Por favor, completa todos los campos correctamente.');
        return;
    }

    // Crear un nuevo elemento de categoría
    const li = document.createElement('li');
    li.className = 'list-group-item d-flex justify-content-between align-items-center';
    li.dataset.categoryId = categoryId;
    li.dataset.allocatedAmount = allocatedAmount;
    li.dataset.threshold = threshold;

    li.innerHTML = `
        ${categoryName} - 
        <span>Asignado: $${allocatedAmount.toFixed(2)} | Umbral: ${threshold}%</span>
        <button class="btn btn-sm btn-danger ms-2" onclick="removeCategory(this)">Eliminar</button>
    `;

    // Agregar la categoría al contenedor
    categoriesContainer.appendChild(li);

    // Limpiar los campos
    categorySelect.value = '';
    allocatedAmountInput.value = '';
    thresholdInput.value = '';
}

// Eliminar dinámicamente una categoría del formulario de presupuestos
function removeCategory(button) {
    const categoryRow = button.closest('.category');
    if (categoryRow) {
        categoryRow.remove();
    }
}

// Cargar presupuestos y poblar la lista
function loadBudgets() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/ProyectoFinal_war_exploded/budgets', true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    const budgets = JSON.parse(xhr.responseText);
                    displayBudgets(budgets);
                    loadTransactionBudgets(budgets);
                } catch (e) {
                    console.error('Error al parsear los presupuestos:', e);
                    alert('Error al cargar los presupuestos.');
                }
            } else {
                alert('Error al cargar los presupuestos: ' + xhr.status + ' - ' + xhr.statusText);
            }
        }
    };

    xhr.send();
}

// Mostrar presupuestos en la lista
function displayBudgets(budgets) {
    const list = document.getElementById('budgetsList');
    if (!list) return;

    list.innerHTML = '';

    budgets.forEach(budget => {
        const categories = Array.isArray(budget.categories) ? budget.categories : [];
        const alerts = generateAlerts(categories);

        const html = `
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">${capitalizeFirstLetter(budget.periodType)}</h5>
                    <p><strong>Inicio:</strong> ${formatDate(budget.periodStart)}</p>
                    <p><strong>Fin:</strong> ${formatDate(budget.periodEnd)}</p>
                    <p><strong>Monto Total:</strong> $${budget.totalAmount.toFixed(2)}</p>
                    <ul>
                        ${categories.map(cat => `
                            <li>
                                <strong>${cat.name}</strong>:
                                Asignado $${cat.allocatedAmount.toFixed(2)},
                                Gastado $${cat.spentAmount.toFixed(2)},
                                Disponible $${(cat.allocatedAmount - cat.spentAmount).toFixed(2)}
                            </li>
                        `).join('')}
                    </ul>
                    ${alerts}
                </div>
            </div>
        `;
        list.innerHTML += html;
    });
}
// Función para cargar categorías basadas en el presupuesto seleccionado
function loadTransactionCategories(budgetId) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `http://localhost:8080/ProyectoFinal_war_exploded/budgets/${budgetId}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    const budget = JSON.parse(xhr.responseText);
                    const categories = Array.isArray(budget.categories) ? budget.categories : [];
                    populateTransactionCategories(categories);
                } catch (e) {
                    console.error('Error al parsear las categorías del presupuesto seleccionado:', e);
                    alert('Error al cargar las categorías del presupuesto seleccionado.');
                }
            } else {
                alert('Error al cargar las categorías del presupuesto seleccionado: ' + xhr.status + ' - ' + xhr.statusText);
            }
        }
    };

    xhr.send();
}

// Función para poblar las categorías del presupuesto seleccionado en el formulario de transacciones
function populateTransactionCategories(categories) {
    const transactionCategorySelect = document.getElementById('transactionCategory');
    if (!transactionCategorySelect) return;

    transactionCategorySelect.innerHTML = '<option value="" disabled selected>Selecciona una categoría</option>';
    categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category.categoryId;
        option.textContent = category.name;
        transactionCategorySelect.appendChild(option);
    });
}

// Cargar presupuestos en el select de transacciones
function loadTransactionBudgets(budgets) {
    const transactionBudgetSelect = document.getElementById('transactionBudget');
    if (!transactionBudgetSelect) return;

    const optionsHtml = budgets.map(budget => {
        return `<option value="${budget.budgetId}">
            ${capitalizeFirstLetter(budget.periodType)} (${formatDate(budget.periodStart)} - ${formatDate(budget.periodEnd)})
        </option>`;
    }).join('');

    transactionBudgetSelect.innerHTML = '<option value="" disabled selected>Selecciona un presupuesto</option>' + optionsHtml;
}

// Configurar eventos
function setupEventListeners() {
    document.getElementById('budgetForm')?.addEventListener('submit', function (e) {
        e.preventDefault();
        createBudget();
    });

    document.getElementById('transactionForm')?.addEventListener('submit', function (e) {
        e.preventDefault();
        createTransaction();
    });

    document.getElementById('transactionBudget')?.addEventListener('change', function () {
        const budgetId = this.value;
        if (budgetId) {
            loadTransactionCategories(budgetId);
        }
    });
}

function createBudget() {
    const form = document.getElementById('budgetForm');
    const formData = new FormData(form);

    const periodType = formData.get('periodType');
    const periodStart = formData.get('periodStart');
    const periodEnd = formData.get('periodEnd');
    const totalAmount = parseFloat(formData.get('totalAmount'));

    // Validaciones
    if (!periodType || !periodStart || !periodEnd || isNaN(totalAmount) || totalAmount <= 0) {
        alert('Por favor, completa todos los campos correctamente.');
        return;
    }

    // Recopilar categorías
    const categoriesContainer = document.getElementById('categoriesContainer').querySelector('ul');
    const categoryItems = categoriesContainer.querySelectorAll('li');
    const categories = Array.from(categoryItems).map(item => ({
        categoryId: item.dataset.categoryId,
        name: item.textContent.split(' - ')[0], // Extrae el nombre
        allocatedAmount: parseFloat(item.dataset.allocatedAmount),
        threshold: parseFloat(item.dataset.threshold)
    }));

    if (categories.length === 0) {
        alert('Debe agregar al menos una categoría al presupuesto.');
        return;
    }

    // Crear el objeto JSON para enviar
    const data = {
        periodType: periodType,
        periodStart: periodStart,
        periodEnd: periodEnd,
        totalAmount: totalAmount,
        categories: categories
    };

    console.log("Datos enviados al servidor:", JSON.stringify(data));

    // Enviar al backend
    sendBudgetToServer(data);
}

// Función para enviar el presupuesto al servidor
function sendBudgetToServer(data) {
    console.log("Datos enviados al servidor:", JSON.stringify(data)); // Para depuración

    // Crear una solicitud AJAX para enviar el presupuesto
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/ProyectoFinal_war_exploded/budgets', true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200 || xhr.status === 201) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (response.status === 'success') {
                        alert('Presupuesto creado exitosamente.');
                        document.getElementById('budgetForm').reset(); // Reiniciar el formulario
                        document.getElementById('categoriesContainer').querySelector('ul').innerHTML = ''; // Limpiar categorías
                        loadBudgets(); // Recargar la lista de presupuestos
                    } else {
                        alert('Error al crear el presupuesto: ' + (response.message || 'Desconocido.'));
                    }
                } catch (e) {
                    console.error('Error al procesar la respuesta del servidor:', e);
                    alert('Error al procesar la respuesta del servidor.');
                }
            } else {
                let errorMsg = 'Error en la solicitud de presupuesto: ' + xhr.status + ' - ' + xhr.statusText;
                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (errorResponse.message) {
                        errorMsg += '\n' + errorResponse.message;
                    }
                } catch (e) {
                    // Ignorar si no es JSON
                }
                alert(errorMsg);
            }
        }
    };

    try {
        xhr.send(JSON.stringify(data));
    } catch (e) {
        console.error('Error al enviar la solicitud:', e);
        alert('Error al enviar la solicitud.');
    }
}


// Registrar una nueva transacción
function createTransaction() {
    const form = document.getElementById('transactionForm');
    const formData = new FormData(form);

    const budgetId = formData.get('transactionBudget'); // Obtener el presupuesto seleccionado
    const categoryId = formData.get('transactionCategory'); // Obtener la categoría seleccionada
    const transactionAmount = parseFloat(formData.get('transactionAmount')); // Monto de la transacción
    const description = formData.get('transactionDescription') || "Sin descripción"; // Descripción de la transacción

    // Validar datos
    if (!budgetId || !categoryId || isNaN(transactionAmount) || transactionAmount <= 0) {
        alert('Por favor, completa todos los campos correctamente.');
        return;
    }

    // Crear objeto JSON para enviar
    const data = {
        budgetId: budgetId,
        categoryId: categoryId,
        amount: transactionAmount,
        description: description
    };

    console.log("Datos enviados para transacción:", JSON.stringify(data));

    // Enviar datos al servidor
    const xhr = new XMLHttpRequest();
    xhr.open('POST', `http://localhost:8080/ProyectoFinal_war_exploded/transactions`, true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    if (response.status === 'success') {
                        alert('Transacción registrada exitosamente.');
                        form.reset();
                        loadBudgets(); // Actualizar la lista de presupuestos
                    } else {
                        alert('Error al registrar la transacción: ' + (response.message || 'Desconocido.'));
                    }
                } catch (e) {
                    console.error('Error al parsear la respuesta del servidor:', e);
                    alert('Error al procesar la respuesta del servidor.');
                }
            } else {
                let errorMsg = `Error en la solicitud de transacción: ${xhr.status} - ${xhr.statusText}`;
                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    if (errorResponse.message) {
                        errorMsg += `\n${errorResponse.message}`;
                    }
                } catch (e) {
                    // Ignorar si no es JSON
                }
                alert(errorMsg);
            }
        }
    };

    try {
        xhr.send(JSON.stringify(data));
    } catch (e) {
        console.error('Error al enviar la solicitud:', e);
        alert('Error al enviar la solicitud.');
    }
    return false;
}


// Enviar POST request
function sendPostRequest(url, data, callback) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                callback();
            } else {
                try {
                    const errorResponse = JSON.parse(xhr.responseText);
                    alert(errorResponse.message || 'Error en el servidor.');
                } catch (e) {
                    alert('Error en la solicitud: ' + xhr.status + ' - ' + xhr.statusText);
                }
            }
        }
    };

    xhr.send(JSON.stringify(data));
}

// Obtener nombre de categoría por ID
function getCategoryName(categoryId) {
    const categoryOptions = document.getElementById('categorySelect')?.options;
    for (let i = 0; i < categoryOptions.length; i++) {
        if (categoryOptions[i].value === categoryId) {
            return categoryOptions[i].textContent;
        }
    }
    return '';
}

// Formatear fechas
function formatDate(dateString) {
    const date = new Date(dateString);
    if (isNaN(date)) return 'Fecha inválida';
    return date.toISOString().split('T')[0];
}

// Capitalizar primera letra
function capitalizeFirstLetter(string) {
    return string ? string.charAt(0).toUpperCase() + string.slice(1) : '';
}

// Generar alertas de gasto
function generateAlerts(categories) {
    return categories.map(cat => {
        const available = cat.allocatedAmount - cat.spentAmount;
        const thresholdAmount = (cat.threshold / 100) * cat.allocatedAmount;

        if (available <= 0) {
            return `<div class="alert alert-danger">La categoría <strong>${cat.name}</strong> ha excedido su presupuesto.</div>`;
        } else if (available <= thresholdAmount) {
            return `<div class="alert alert-warning">La categoría <strong>${cat.name}</strong> está cerca de alcanzar el límite de gasto.</div>`;
        }
        return '';
    }).join('');
}
// Cargar transacciones y mostrarlas en el dashboard
function loadTransactions() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/ProyectoFinal_war_exploded/transactions', true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    const transactions = JSON.parse(xhr.responseText);
                    displayTransactions(transactions);
                } catch (e) {
                    console.error('Error al parsear las transacciones:', e);
                    alert('Error al cargar las transacciones.');
                }
            } else {
                alert('Error al cargar las transacciones: ' + xhr.status + ' - ' + xhr.statusText);
            }
        }
    };

    xhr.send();
}
// Cargar presupuestos en el filtro de transacciones
function loadTransactionFilterBudgets() {
    const transactionBudgetFilter = document.getElementById('transactionBudgetFilter');
    if (!transactionBudgetFilter) return;

    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/ProyectoFinal_war_exploded/budgets', true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    const budgets = JSON.parse(xhr.responseText);
                    transactionBudgetFilter.innerHTML = '<option value="" disabled selected>Selecciona un presupuesto</option>';
                    budgets.forEach(budget => {
                        const option = document.createElement('option');
                        option.value = budget.budgetId; // Asegúrate de usar la propiedad correcta
                        option.textContent = `${capitalizeFirstLetter(budget.periodType)} (${formatDate(budget.periodStart)} - ${formatDate(budget.periodEnd)})`;
                        transactionBudgetFilter.appendChild(option);
                    });
                } catch (e) {
                    console.error('Error al parsear los presupuestos para el filtro:', e);
                    alert('Error al cargar los presupuestos para el filtro.');
                }
            } else {
                alert('Error al cargar los presupuestos para el filtro: ' + xhr.status + ' - ' + xhr.statusText);
            }
        }
    };

    xhr.send();
}

// Filtrar transacciones según el presupuesto seleccionado
function filterTransactions() {
    const transactionBudgetFilter = document.getElementById('transactionBudgetFilter');
    const budgetId = transactionBudgetFilter.value;

    if (!budgetId) {
        alert('Por favor, selecciona un presupuesto.');
        return;
    }

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `http://localhost:8080/ProyectoFinal_war_exploded/transactions?budgetId=${budgetId}`, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    const transactions = JSON.parse(xhr.responseText);
                    displayTransactions(transactions);
                } catch (e) {
                    console.error('Error al parsear las transacciones:', e);
                    alert('Error al cargar las transacciones.');
                }
            } else {
                alert('Error al cargar las transacciones: ' + xhr.status + ' - ' + xhr.statusText);
            }
        }
    };

    xhr.send();
}

// Mostrar las transacciones en el contenedor correspondiente
function displayTransactions(transactions) {
    const container = document.getElementById('transactionsContainer');
    if (!container) return;

    container.innerHTML = ''; // Limpiar contenido anterior

    if (transactions.length === 0) {
        container.innerHTML = '<p class="text-center">No hay transacciones registradas para este presupuesto.</p>';
        return;
    }

    transactions.forEach(transaction => {
        const date = transaction.date ? formatDate(transaction.date) : 'Fecha no disponible';
        const html = `
            <div class="col-md-4">
                <div class="card mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Categoría: ${transaction.categoryName || 'Desconocida'}</h5>
                        <p><strong>Monto:</strong> $${transaction.amount.toFixed(2)}</p>
                        <p><strong>Descripción:</strong> ${transaction.description || 'Sin descripción'}</p>
                        <p><strong>Fecha:</strong> ${date}</p>
                    </div>
                </div>
            </div>
        `;
        container.innerHTML += html;
    });
}
