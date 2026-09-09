const news = [
  {
    category: "GENERAL",
    title: "Elche aprueba un plan para mejorar la movilidad y reducir el tráfico en el centro",
    summary: "El Ayuntamiento destinará más de 12 millones de euros a peatonalizaciones, nuevas zonas verdes y transporte público sostenible.",
    date: "20 de mayo de 2025"
  },
  {
    category: "ECONOMÍA",
    title: "La provincia de Alicante lidera el crecimiento empresarial en la Comunitat Valenciana",
    summary: "En el primer trimestre del año se han creado más de 1.300 nuevas empresas, un 18% más que en el mismo periodo de 2024.",
    date: "20 de mayo de 2025"
  },
  {
    category: "SUCESOS",
    title: "Rescatan a un senderista herido en la Sierra de Aitana",
    summary: "El herido, un varón de 34 años, fue evacuado en helicóptero hasta el Hospital de La Vila Joiosa.",
    date: "20 de mayo de 2025"
  },
  {
    category: "DEPORTES",
    title: "El Hércules CF asegura su plaza en el playoff de ascenso",
    summary: "El equipo alicantino empata en casa y certifica su clasificación a falta de una jornada para el final de la liga.",
    date: "20 de mayo de 2025"
  },
  {
    category: "CULTURA",
    title: "El MARQ inaugura una exposición sobre los íberos en la provincia de Alicante",
    summary: "La muestra reúne más de 200 piezas arqueológicas y podrá visitarse hasta el próximo mes de octubre.",
    date: "20 de mayo de 2025"
  },
  {
    category: "SOCIEDAD",
    title: "Alicante refuerza la limpieza de playas de cara al inicio del verano",
    summary: "El servicio se ampliará a partir del 1 de junio con más personal y nuevos equipos de cribado de arena.",
    date: "20 de mayo de 2025"
  }
];

const newsList = document.getElementById("newsList");
const emptyState = document.getElementById("emptyState");
//const menuButton = document.getElementById("menuButton");
const mainNav = document.getElementById("mainNav");
const searchButton = document.getElementById("searchButton");
const searchPanel = document.getElementById("searchPanel");
const searchInput = document.getElementById("searchInput");

document.getElementById("year").textContent = new Date().getFullYear();

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function renderNews(items) {
  newsList.innerHTML = items.map((item, index) => `
    <article class="news-card" data-index="${index}">
      <p class="news-category">${escapeHtml(item.category)}</p>
      <h2 class="news-title">${escapeHtml(item.title)}</h2>
      <p class="news-summary">${escapeHtml(item.summary)}</p>
      <p class="news-meta">${escapeHtml(item.date)}</p>
    </article>
  `).join("");

  emptyState.hidden = items.length !== 0;
}

renderNews(news);

/*
menuButton.addEventListener("click", () => {
  const open = menuButton.classList.toggle("open");
  menuButton.setAttribute("aria-expanded", String(open));
  mainNav.classList.toggle("mobile-open", open);

  if (open && window.innerWidth <= 700) {
    mainNav.scrollIntoView({ behavior: "smooth", block: "nearest" });
  }
});
*/

searchButton.addEventListener("click", () => {
  const open = searchPanel.classList.toggle("open");
  if (open) {
    searchInput.focus();
  } else {
    searchInput.value = "";
    renderNews(news);
  }
});

searchPanel.addEventListener("submit", (event) => {
  event.preventDefault();

  const query = searchInput.value.trim().toLowerCase();

  if (!query) {
    renderNews(news);
    return;
  }

  const filtered = news.filter(item =>
    [item.category, item.title, item.summary].some(value =>
      value.toLowerCase().includes(query)
    )
  );

  renderNews(filtered);
});

mainNav.addEventListener("click", (event) => {
  const link = event.target.closest("a");
  if (!link) return;

  event.preventDefault();
  mainNav.querySelectorAll("a").forEach(item => item.classList.remove("active"));
  link.classList.add("active");

  const section = link.textContent.trim().toLowerCase();
  if (section === "general") {
    renderNews(news);
    return;
  }

  renderNews(news.filter(item => item.category.toLowerCase() === section));
});
