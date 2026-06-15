/* ============================================
   KarirKu — Frontend App
   Connects to Spring Boot: http://localhost:8080
   ============================================ */

const API = 'http://localhost:8080/api';

// State
let allJobs = [];
let activeLowonganId = null;

// Palette logo per inisial
const LOGO_COLORS = [
  { bg: '#EEF2FF', text: '#3730A3' },
  { bg: '#F0FDF4', text: '#166534' },
  { bg: '#FFF7ED', text: '#9A3412' },
  { bg: '#FDF4FF', text: '#7E22CE' },
  { bg: '#FEF2F2', text: '#991B1B' },
  { bg: '#ECFDF5', text: '#065F46' },
];

function logoColor(name) {
  let hash = 0;
  for (const c of (name || '')) hash = (hash * 31 + c.charCodeAt(0)) & 0xffffffff;
  return LOGO_COLORS[Math.abs(hash) % LOGO_COLORS.length];
}

function initials(name) {
  if (!name) return '??';
  const words = name.trim().split(/\s+/);
  if (words.length === 1) return words[0].slice(0, 2).toUpperCase();
  return (words[0][0] + words[1][0]).toUpperCase();
}

function formatGaji(min, max) {
  const fmt = v => {
    if (!v) return null;
    const n = parseInt(v);
    if (isNaN(n)) return v;
    return 'Rp ' + (n >= 1_000_000
      ? (n / 1_000_000).toFixed(n % 1_000_000 === 0 ? 0 : 1) + ' jt'
      : (n / 1000).toFixed(0) + 'rb');
  };
  const fmin = fmt(min), fmax = fmt(max);
  if (fmin && fmax) return `${fmin} – ${fmax}`;
  if (fmin) return `dari ${fmin}`;
  if (fmax) return `s/d ${fmax}`;
  return null;
}

function timeAgo(isoString) {
  if (!isoString) return '';
  const diff = Date.now() - new Date(isoString).getTime();
  const m = Math.floor(diff / 60000);
  if (m < 1) return 'Baru saja';
  if (m < 60) return `${m} menit lalu`;
  const h = Math.floor(m / 60);
  if (h < 24) return `${h} jam lalu`;
  const d = Math.floor(h / 24);
  if (d < 7) return `${d} hari lalu`;
  return new Date(isoString).toLocaleDateString('id-ID', { day: 'numeric', month: 'short' });
}

function isNew(isoString) {
  if (!isoString) return false;
  return Date.now() - new Date(isoString).getTime() < 24 * 60 * 60 * 1000;
}

// ---- LOAD JOBS ----
async function loadJobs() {
  document.getElementById('apiError').style.display = 'none';
  renderSkeleton();

  try {
    const [jobsRes, statsRes] = await Promise.all([
      fetch(`${API}/lowongan`),
      fetch(`${API}/lowongan/stats`),
    ]);

    if (!jobsRes.ok) throw new Error('Response tidak OK');

    allJobs = await jobsRes.json();

    if (statsRes.ok) {
      const stats = await statsRes.json();
      document.getElementById('statTotal').textContent = stats.totalLowonganAktif ?? allJobs.length;
    } else {
      document.getElementById('statTotal').textContent = allJobs.length;
    }

    applyFilters();
  } catch (err) {
    console.error('API Error:', err);
    document.getElementById('apiError').style.display = 'flex';
    document.getElementById('jobList').innerHTML = '';
    document.getElementById('resultCount').textContent = 'Gagal memuat lowongan';
    document.getElementById('statTotal').textContent = '–';
  }
}

// ---- SEARCH ----
function doSearch() {
  applyFilters();
}

// Enter key on search
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('searchInput').addEventListener('keydown', e => {
    if (e.key === 'Enter') doSearch();
  });
  document.getElementById('lokasiInput').addEventListener('keydown', e => {
    if (e.key === 'Enter') doSearch();
  });
  loadJobs();
});

// ---- FILTER & RENDER ----
function applyFilters() {
  const keyword = document.getElementById('searchInput').value.toLowerCase().trim();
  const lokasi  = document.getElementById('lokasiInput').value.toLowerCase().trim();
  const remoteOnly = document.getElementById('filterRemote').checked;

  const checkedJenis = [...document.querySelectorAll('.filter-check input[type=checkbox]:checked')]
    .map(cb => cb.value)
    .filter(v => ['FULL_TIME','PART_TIME','FREELANCE','INTERNSHIP'].includes(v));

  const checkedKategori = [...document.querySelectorAll('.filter-check input[type=checkbox]:checked')]
    .map(cb => cb.value)
    .filter(v => ['TEKNOLOGI','DESAIN','MARKETING','KEUANGAN','OPERASIONAL','LAINNYA'].includes(v));

  let list = allJobs.filter(j => {
    const matchKw = !keyword ||
      j.judul?.toLowerCase().includes(keyword) ||
      j.perusahaan?.toLowerCase().includes(keyword) ||
      j.deskripsi?.toLowerCase().includes(keyword);

    const matchLok = !lokasi ||
      j.lokasi?.toLowerCase().includes(lokasi) ||
      (lokasi === 'remote' && j.remote);

    const matchJenis = checkedJenis.length === 0 || checkedJenis.includes(j.jenis);
    const matchKat   = checkedKategori.length === 0 || checkedKategori.includes(j.kategori);
    const matchRemote = !remoteOnly || j.remote;

    return matchKw && matchLok && matchJenis && matchKat && matchRemote;
  });

  const sort = document.getElementById('sortSelect').value;
  if (sort === 'featured') {
    list = [...list].sort((a, b) => (b.featured ? 1 : 0) - (a.featured ? 1 : 0));
  }

  renderJobs(list);
}

function resetFilters() {
  document.getElementById('searchInput').value = '';
  document.getElementById('lokasiInput').value = '';
  document.getElementById('filterRemote').checked = false;
  document.querySelectorAll('.filter-check input[type=checkbox]').forEach(cb => cb.checked = true);
  applyFilters();
}

// ---- RENDER CARDS ----
function renderJobs(list) {
  const container = document.getElementById('jobList');
  const empty     = document.getElementById('emptyState');
  const count     = document.getElementById('resultCount');

  if (list.length === 0) {
    container.innerHTML = '';
    empty.style.display = 'block';
    count.textContent = 'Tidak ada hasil';
    return;
  }

  empty.style.display = 'none';
  count.textContent = `${list.length} lowongan ditemukan`;

  container.innerHTML = list.map((j, idx) => {
    const lc    = logoColor(j.perusahaan);
    const gaji  = formatGaji(j.gajiMin, j.gajiMax);
    const jenis = labelJenis(j.jenis);
    const kNew  = isNew(j.tanggalDibuat);

    return `
    <div class="job-card" data-kategori="${j.kategori || ''}"
         style="animation-delay: ${idx * 40}ms"
         onclick="openLamarModal(${j.id}, '${escHtml(j.judul)}', '${escHtml(j.perusahaan)}')">
      <div class="job-logo" style="background:${lc.bg}; color:${lc.text}">
        ${initials(j.perusahaan)}
      </div>
      <div class="job-body">
        <div class="job-top">
          <div>
            <div class="job-title">${escHtml(j.judul)}</div>
            <div class="job-company">${escHtml(j.perusahaan)}</div>
          </div>
          <div style="display:flex;gap:6px;flex-shrink:0">
            ${j.featured ? '<span class="badge-featured">⭐ Featured</span>' : ''}
            ${kNew ? '<span class="badge-new">Baru</span>' : ''}
          </div>
        </div>
        <div class="job-tags">
          <span class="tag tag-type">${jenis}</span>
          <span class="tag tag-loc">📍 ${escHtml(j.lokasi)}</span>
          ${gaji ? `<span class="tag tag-sal">💰 ${gaji}</span>` : ''}
          ${j.remote ? '<span class="tag tag-remote">🌐 Remote</span>' : ''}
        </div>
        ${j.deskripsi ? `<div class="job-desc">${escHtml(j.deskripsi)}</div>` : ''}
        <div class="job-footer">
          <span class="job-time">${timeAgo(j.tanggalDibuat)}</span>
          <button class="btn-lamar" onclick="event.stopPropagation(); openLamarModal(${j.id}, '${escHtml(j.judul)}', '${escHtml(j.perusahaan)}')">
            Lamar Sekarang →
          </button>
        </div>
      </div>
    </div>`;
  }).join('');
}

function renderSkeleton() {
  const skels = Array(4).fill(0).map(() => `
    <div class="skeleton">
      <div class="sk-box sk-logo"></div>
      <div class="sk-body">
        <div class="sk-box sk-title"></div>
        <div class="sk-box sk-sub"></div>
        <div class="sk-box sk-tags"></div>
        <div class="sk-box sk-desc"></div>
        <div class="sk-box sk-desc2"></div>
      </div>
    </div>`).join('');
  document.getElementById('jobList').innerHTML = skels;
  document.getElementById('resultCount').textContent = 'Memuat lowongan…';
}

function labelJenis(v) {
  const map = { FULL_TIME: 'Full-time', PART_TIME: 'Part-time', FREELANCE: 'Freelance', INTERNSHIP: 'Magang' };
  return map[v] || v || '–';
}

function escHtml(s) {
  return (s || '').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;').replace(/'/g,'&#39;');
}

// ---- MODAL ----
function openModal(id) {
  document.getElementById(id).classList.add('open');
  document.body.style.overflow = 'hidden';
}
function closeModal(id) {
  document.getElementById(id).classList.remove('open');
  document.body.style.overflow = '';
}
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-bg')) {
    e.target.classList.remove('open');
    document.body.style.overflow = '';
  }
});

function openLamarModal(id, judul, perusahaan) {
  activeLowonganId = id;
  document.getElementById('lamar_judul').textContent = judul;
  document.getElementById('lamar_perusahaan').textContent = perusahaan;
  document.getElementById('lamarError').style.display = 'none';
  ['l_nama','l_email','l_telp','l_cover','l_portfolio'].forEach(f => document.getElementById(f).value = '');
  openModal('lamarModal');
}

// ---- SUBMIT LAMARAN ----
async function submitLamaran() {
  const errEl = document.getElementById('lamarError');
  errEl.style.display = 'none';

  const nama  = document.getElementById('l_nama').value.trim();
  const email = document.getElementById('l_email').value.trim();

  if (!nama)  { showFormError(errEl, 'Nama lengkap wajib diisi.'); return; }
  if (!email) { showFormError(errEl, 'Email wajib diisi.'); return; }
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    showFormError(errEl, 'Format email tidak valid.'); return;
  }

  const payload = {
    namaLengkap: nama,
    email,
    noTelepon:     document.getElementById('l_telp').value.trim() || null,
    coverLetter:   document.getElementById('l_cover').value.trim() || null,
    linkPortfolio: document.getElementById('l_portfolio').value.trim() || null,
  };

  const btn = document.querySelector('#lamarModal .btn-primary');
  btn.textContent = 'Mengirim…';
  btn.disabled = true;

  try {
    const res = await fetch(`${API}/lowongan/${activeLowonganId}/lamar`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    const data = await res.json();

    if (res.ok) {
      closeModal('lamarModal');
      showToast('✅ Lamaran berhasil dikirim! Tim rekrutmen akan menghubungimu.', 'success');
    } else {
      showFormError(errEl, data.error || 'Terjadi kesalahan. Coba lagi.');
    }
  } catch {
    showFormError(errEl, 'Tidak dapat terhubung ke server. Pastikan backend berjalan.');
  } finally {
    btn.textContent = 'Kirim Lamaran';
    btn.disabled = false;
  }
}

// ---- SUBMIT LOWONGAN BARU ----
async function submitLowongan() {
  const errEl = document.getElementById('postError');
  errEl.style.display = 'none';

  const judul      = document.getElementById('p_judul').value.trim();
  const perusahaan = document.getElementById('p_perusahaan').value.trim();
  const lokasi     = document.getElementById('p_lokasi').value.trim();
  const deskripsi  = document.getElementById('p_deskripsi').value.trim();

  if (!judul)      { showFormError(errEl, 'Judul posisi wajib diisi.'); return; }
  if (!perusahaan) { showFormError(errEl, 'Nama perusahaan wajib diisi.'); return; }
  if (!lokasi)     { showFormError(errEl, 'Lokasi wajib diisi.'); return; }
  if (!deskripsi)  { showFormError(errEl, 'Deskripsi pekerjaan wajib diisi.'); return; }

  const payload = {
    judul,
    perusahaan,
    lokasi,
    jenis:       document.getElementById('p_jenis').value,
    kategori:    document.getElementById('p_kategori').value,
    remote:      document.getElementById('p_remote').value === 'true',
    gajiMin:     document.getElementById('p_gajiMin').value.trim() || null,
    gajiMax:     document.getElementById('p_gajiMax').value.trim() || null,
    deskripsi,
    kualifikasi: document.getElementById('p_kualifikasi').value.trim() || null,
  };

  const btn = document.querySelector('#postModal .btn-primary');
  btn.textContent = 'Memproses…';
  btn.disabled = true;

  try {
    const res = await fetch(`${API}/lowongan`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    const data = await res.json();

    if (res.ok || res.status === 201) {
      closeModal('postModal');
      showToast('✅ Lowongan berhasil dipasang!', 'success');
      // Reload list dan tambahkan job baru
      await loadJobs();
    } else {
      const msg = data.judul || data.perusahaan || data.error || JSON.stringify(data);
      showFormError(errEl, msg);
    }
  } catch {
    showFormError(errEl, 'Tidak dapat terhubung ke server. Pastikan backend berjalan.');
  } finally {
    btn.textContent = 'Pasang Sekarang';
    btn.disabled = false;
  }
}

// ---- HELPERS ----
function showFormError(el, msg) {
  el.textContent = msg;
  el.style.display = 'block';
}

let toastTimer;
function showToast(msg, type = '') {
  clearTimeout(toastTimer);
  const t = document.getElementById('toast');
  t.textContent = msg;
  t.className = 'toast show' + (type ? ' ' + type : '');
  toastTimer = setTimeout(() => t.className = 'toast', 3800);
}
