<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<%@ include file="navbar.jsp" %>
<body>
<div class="container">

    <div class="quest-container" style="margin-top: 0;">
        <%@ include file="character.jsp" %>

        <div class="ability-container">
            <!-- Выбор новых способностей -->
            <div>
                <h4>Выбрать способности</h4>
                <form action="${pageContext.request.contextPath}/profile" method="post">
                    <div class="abilities-grid">
                        <c:forEach var="ability" items="${sessionScope.allAbilities}">
                            <c:if test="${ability.levelRequirement <= player.level}">
                                <label class="ability-card">
                                    <input type="checkbox" name="abilityIds" value="${ability.id}"
                                           <c:if test="${player.abilities.contains(ability)}">checked</c:if>
                                           class="ability-checkbox">
                                    <div class="ability-content">
                                        <div class="ability-name">${ability.name}</div>
                                        <div class="ability-stats">
                                            <c:if test="${ability.type == 'HEAL'}">
                                                <span>❤️️${ability.value}</span>
                                            </c:if>
                                            <c:if test="${ability.type == 'DAMAGE'}">
                                                <span>🗡️${ability.value}</span>
                                            </c:if>
                                            <span>⏱️${ability.cooldown}</span>
                                            <span>📊${ability.levelRequirement}+</span>
                                        </div>
                                        <div class="ability-tooltip">${ability.description}</div>
                                    </div>
                                </label>
                            </c:if>
                        </c:forEach>

                    </div>
                    <div class="selection-info">
                        Выбрано: <span id="selected-count">0</span>/4
                    </div>
                    <button type="submit" class="btn-success" id="submit-btn">Применить способности</button>
                </form>
            </div>

        </div>


    </div>
    <form action="/logout" method="post">
        <button type="submit" class="btn btn-menu">Выйти</button>
    </form>
</div>
    <style>

        .selection-info {
            text-align: center;
            margin: 20px 0;
            font-size: 16px;
            color: #2c3e50;
            font-weight: bold;
        }

        #submit-btn {
            display: block;
            margin: 0 auto;
            padding: 12px 25px;
            font-size: 14px;
        }

        .btn-success {
            background: linear-gradient(135deg, #27ae60, #2ecc71);
            border: none;
            color: white;
            padding: 10px 20px;
            border-radius: 6px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-success:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(39, 174, 96, 0.4);
        }



        /* Адаптивность */
        @media (max-width: 768px) {
            /*.ability-container {*/
            /*    grid-template-columns: 1fr;*/
            /*    gap: 20px;*/
            /*}*/

            .ability-container > div:first-child {
                border-right: none;
                border-bottom: 2px solid #ecf0f1;
                padding-right: 0;
                padding-bottom: 20px;
            }

            .ability-container > div:last-child {
                padding-left: 0;
            }

            /*.abilities-grid {*/
            /*    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));*/
            /*}*/
        }
    </style>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const checkboxes = document.querySelectorAll('.ability-checkbox');
            const selectedCount = document.getElementById('selected-count');
            const submitBtn = document.getElementById('submit-btn');

            function updateSelection() {
                const selected = document.querySelectorAll('.ability-checkbox:checked');
                const count = selected.length;

                selectedCount.textContent = count;

                if (count > 4) {
                    submitBtn.disabled = true;
                    submitBtn.style.opacity = '0.6';
                } else {
                    submitBtn.disabled = false;
                    submitBtn.style.opacity = '1';
                }

                checkboxes.forEach(checkbox => {
                    const card = checkbox.closest('.ability-card');
                    if (checkbox.checked) {
                        card.classList.add('selected');
                    } else {
                        card.classList.remove('selected');
                    }
                });
            }

            checkboxes.forEach(checkbox => {
                checkbox.addEventListener('change', function () {
                    const selected = document.querySelectorAll('.ability-checkbox:checked');
                    if (selected.length > 4) {
                        this.checked = false;
                    }
                    updateSelection();
                });
            });
            updateSelection();
        });
    </script>
</body>


