function handleScroll() {
    const goToTopButton = document.getElementById('go-to-top');
    if (window.scrollY > 100) {
      goToTopButton.style.display = 'block';
    } else {
      goToTopButton.style.display = 'none';
    }
  }
  
  window.addEventListener('scroll', handleScroll);
  
  function scrollToTop() {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  ReactDOM.render(
    React.createElement(ScroolToTop),
    document.getElementById('scroll-to-top-container')
  );
  
function hienThiDuongDan() {
    var duongDanContainer = document.getElementById("duongDanContainer");
    if (duongDanContainer.classList.contains("hidden-content")) {
        duongDanContainer.classList.remove("hidden-content");
        fetchKhoaFromAPI();
    } else {
        duongDanContainer.classList.add("hidden-content");
    }
}

function fetchKhoaFromAPI() {
    fetch("http://47.254.215.213/vku/admin/api/studentTuition")
        .then(response => response.json())
        .then(data => displayKhoa(data))
        .catch(error => console.error("Error fetching data:", error));
}

function displayKhoa(data) {
    var khoaElement = document.getElementById("khoa1");
    if (data && data.length > 0) {
        var khoaCounts = {};
        data.forEach(item => {
            var khoa = item.idStudent.khoa.khoa;
            khoaCounts[khoa] = (khoaCounts[khoa] || 0) + 1;
        });

        var html = "";
        for (var khoa in khoaCounts) {
            html += `<a href="/admin/statisticsMoney/khoa" class="dropdown-item notify-item">`;
            html += `${khoa}: ${khoaCounts[khoa]}`;
            html += `</a>`;
        }

        khoaElement.innerHTML = html;
    } else {
        khoaElement.textContent = "Thống kê học phí không có dữ liệu";
    }
}

// Fetch the khoa data when the page loads (optional)
window.onload = function () {
    fetchKhoaFromAPI();
};

function hienThiDuongDan1() {
    var duongDanContainer1 = document.getElementById("duongDanContainer1");
    if (duongDanContainer1.classList.contains("hidden-content")) {
        duongDanContainer1.classList.remove("hidden-content");
        fetchNganhFromAPI();
    } else {
        duongDanContainer1.classList.add("hidden-content");
    }
}

function fetchNganhFromAPI() {
    fetch("http://47.254.215.213/vku/admin/api/studentTuition")
        .then(response => response.json())
        .then(data => displayNganh(data))
        .catch(error => console.error("Error fetching data:", error));
}

function displayNganh(data) {
    var nganhElement = document.getElementById("nganh");
    var nganhLinkElement = document.getElementById("nganhLink");

    if (data && data.length > 0) {
        var nganhCounts = {};
        data.forEach(item => {
            var nganh = item.idStudent.majors.majorsName;
            nganhCounts[nganh] = (nganhCounts[nganh] || 0) + 1;
        });

        var html = "";
        for (var nganh in nganhCounts) {
            html += `<a href="/admin/statisticsMoney/nganh" class="dropdown-item notify-item color">`;
            html += `${nganh}: ${nganhCounts[nganh]}`;
            html += `</a>`;
        }

        nganhElement.textContent = "";
        nganhLinkElement.innerHTML = html;
    } else {
        nganhElement.textContent = "Thống kê học phí không có dữ liệu";
        nganhLinkElement.innerHTML = "";
    }
}

window.onload = function () {
    fetchNganhFromAPI();
};


function hienThiDuongDan2() {
    var duongDanContainer2 = document.getElementById("duongDanContainer2");
    if (duongDanContainer2.classList.contains("hidden-content")) {
        duongDanContainer2.classList.remove("hidden-content");
        fetchNgayFromAPI();
    } else {
        duongDanContainer2.classList.add("hidden-content");
    }
}



function fetchNgayFromAPI() {
    fetch("http://47.254.215.213/vku/admin/api/studentTuition")
     //fetch("http://localhost:2222/admin/api/studentTuition")

        .then(response => response.json())
        .then(data => displayNgay(data))
        .catch(error => console.error("Error fetching data:", error));
}

function displayNgay(data) {
    var ngayElement = document.getElementById("ngay");
    if (data && data.length > 0) {
        var ngayCounts = {};
        data.forEach(item => {
            var ngay = item.tuitionDay;
            ngayCounts[ngay] = (ngayCounts[ngay] || 0) + 1;
        });

        var html = "";
        for (var ngay in ngayCounts) {
            html += `<a href="/admin/statisticsMoney/ngay" class="dropdown-item notify-item">`;
            html += `${ngay}: ${ngayCounts[ngay]}`;
            html += `</a>`;
        }

        ngayElement.innerHTML = html;
    } else {
        ngayElement.textContent = "Thống kê ngày không có dữ liệu";
    }
}

// Fetch the ngay data when the page loads (optional)
window.onload = function () {
    fetchNgayFromAPI();
};

// JavaScript
function hienThiDuongDan3() {
    var duongDanContainer3 = document.getElementById("duongDanContainer3");
    if (duongDanContainer3.classList.contains("hidden-content")) {
        duongDanContainer3.classList.remove("hidden-content");
        fetchNguoiThuFromAPI();
    } else {
        duongDanContainer3.classList.add("hidden-content");
    }
}

function fetchNguoiThuFromAPI() {
    fetch("http://47.254.215.213/vku/admin/api/studentTuition")
        .then(response => response.json())
        .then(data => displayNguoiThu(data))
        .catch(error => console.error("Error fetching data:", error));
}

function displayNguoiThu(data) {
    var nguoiThuElement = document.getElementById("nguoithu");
    if (data && data.length > 0) {
        var nguoiThuCounts = {};
        data.forEach(item => {
            var nguoiThu = item.nameCashier;
            nguoiThuCounts[nguoiThu] = (nguoiThuCounts[nguoiThu] || 0) + 1;
        });

        var html = "";
        for (var nguoiThu in nguoiThuCounts) {
            html += `<a href="/admin/statisticsMoney/nguoithu" class="dropdown-item notify-item">`;
            html += `${nguoiThu}: ${nguoiThuCounts[nguoiThu]}`;
            html += `</a>`;
        }

        nguoiThuElement.innerHTML = html;
    } else {
        nguoiThuElement.textContent = "Thống kê người thu không có dữ liệu";
    }
}

// Fetch the nguoi thu data when the page loads (optional)
window.onload = function () {
    fetchNguoiThuFromAPI();
};
function hienThiDuongDan4() {
    var duongDanContainer4 = document.getElementById("duongDanContainer4");
    if (duongDanContainer4.classList.contains("hidden-content")) {
        duongDanContainer4.classList.remove("hidden-content");
        fetchSoLuongNopFromAPI();
    } else {
        duongDanContainer4.classList.add("hidden-content");
    }
}

function fetchSoLuongNopFromAPI() {
    fetch("http://47.254.215.213/vku/admin/api/studentTuition")
        .then(response => response.json())
        .then(data => displaySoLuongNop(data))
        .catch(error => console.error("Error fetching data:", error));
}

function displaySoLuongNop(data) {
    var soLuongNopElement = document.getElementById("soluongnop");
    if (data && data.length > 0) {
        var count = 0;
        data.forEach(item => {
            if (item.total > 0) {
                count++;
            }
        });

        soLuongNopElement.textContent = count;
    } else {
        soLuongNopElement.textContent = "0";
    }
}

// Fetch the So Luong Nop data when the page loads (optional)
window.onload = function () {
    fetchSoLuongNopFromAPI();
};
 function hienThiDuongDan5() {
        var duongDanContainer5 = document.getElementById("duongDanContainer5");
        if (duongDanContainer5.classList.contains("hidden-content")) {
            duongDanContainer5.classList.remove("hidden-content");
            fetchTuitionData();
        } else {
            duongDanContainer5.classList.add("hidden-content");
        }
    }

    function fetchTuitionData() {
        fetch("http://47.254.215.213/vku/admin/api/studentTuition")
            .then(response => response.json())
            .then(data => {
                const countResult = countTuitionFees(data);
                displayTuitionFeeCount(countResult);
            })
            .catch(error => console.error("Error fetching data:", error));
    }

    function countTuitionFees(data) {
        const tuitionFeeList = data.map(item => item.tuitionFeeList).join(",").split(",").map(Number);
        const tuitionFeeCount = {};

        for (const fee of tuitionFeeList) {
            if (tuitionFeeCount[fee]) {
                tuitionFeeCount[fee]++;
            } else {
                tuitionFeeCount[fee] = 1;
            }
        }

        return tuitionFeeCount;
    }

    function displayTuitionFeeCount(countResult) {
        var nganhElement = document.getElementById("nganh1");
    var htmlContent = "<ul>";

    for (const [tuitionFee, count] of Object.entries(countResult)) {
        htmlContent += `<li><p class="color">Loại học phí ${tuitionFee}: ${count}</p></li>`;
    }

    htmlContent += "</ul>";
    nganhElement.innerHTML = htmlContent;
    }
;
//

    function hienThiDuongDan8() {
        var duongDanContainer8 = document.getElementById("duongDanContainer8");
        if (duongDanContainer8.classList.contains("hidden-content")) {
            duongDanContainer8.classList.remove("hidden-content");
            fetchDocumentData();
        } else {
            duongDanContainer8.classList.add("hidden-content");
        }
    }

    function fetchDocumentData() {
        fetch("http://47.254.215.213/vku/admin/api/document/getAll")
            .then(response => response.json())
            .then(data => {
                const countResult = calculateDocumentItemCount(data);
                displayDocumentItemCount(countResult);
            })
            .catch(error => console.error("Error fetching data:", error));
    }

    function calculateDocumentItemCount(data) {
        const documentItemCount = {};

        data.forEach(item => {
            const documentType = item.documentType;
            const count = item.soLuong;
            documentItemCount[documentType] = (documentItemCount[documentType] || 0) + count;
        });

        return documentItemCount;
    }

    function displayDocumentItemCount(countResult) {
        var duongDanContainer8 = document.getElementById("duongDanContainer8");
        var ulElement = duongDanContainer8.querySelector("ul");
        ulElement.innerHTML = "";

        for (const [documentType, count] of Object.entries(countResult)) {
            const liElement = document.createElement("li");
            liElement.innerHTML = `<p class="color">${documentType + ': ' + count}</p>`;
            ulElement.appendChild(liElement);
        }
    }

    // Fetch the document data when the page loads (optional)
    window.onload = function () {
        fetchDocumentData();
    };
//
function hienThiDuongDan9() {
    var duongDanContainer9 = document.getElementById("duongDanContainer9");
    if (duongDanContainer9.classList.contains("hidden-content")) {
        duongDanContainer9.classList.remove("hidden-content");
        fetchDocumentData();
    } else {
        duongDanContainer9.classList.add("hidden-content");
    }
}

function fetchDocumentData() {
    fetch("http://47.254.215.213/vku/admin/api/document/getAll")
        .then(response => response.json())
        .then(data => {
            const countResult = calculateDocumentItemCount(data);
            const totalStudentsCount = calculateTotalStudentsCount(data);
            displayDocumentItemCount(countResult, totalStudentsCount);
        })
        .catch(error => console.error("Error fetching data:", error));
}

function calculateDocumentItemCount(data) {
    const documentItemCount = {};

    data.forEach(item => {
        const documentType = item.documentType;
        const count = item.soLuong;
        documentItemCount[documentType] = (documentItemCount[documentType] || 0) + count;
    });

    return documentItemCount;
}

function calculateTotalStudentsCount(data) {
    let totalStudentsCount = 0;
    data.forEach(item => {
        totalStudentsCount += item.soLuong;
    });
    return totalStudentsCount;
}

function displayDocumentItemCount(countResult, totalStudentsCount) {
    var duongDanContainer9 = document.getElementById("duongDanContainer9");
    var ulElement = duongDanContainer9.querySelector("ul");
    ulElement.innerHTML = "";

    // Iterate through countResult object and add items to the list
    for (const documentType in countResult) {
        const liElement = document.createElement("li");
        liElement.innerHTML = `<p class="color">${documentType}: ${countResult[documentType]}</p>`;
        ulElement.appendChild(liElement);
    }

    // Add total students count
    const liElement = document.createElement("li");
    liElement.innerHTML = `<p class="color">Tổng số thí sinh đã nộp giấy tờ: ${totalStudentsCount}</p>`;
    ulElement.appendChild(liElement);
}

// Fetch the document data when the page loads (optional)
window.onload = function () {
    fetchDocumentData();
};


function khoaajax1() {
				var duongDanContainer1 = document.getElementById("khoacontainer1");
				if (duongDanContainer1.classList.contains("hidden-content")) {
					duongDanContainer1.classList.remove("hidden-content");
					fetchKhoaFromAPI();
				} else {
					duongDanContainer1.classList.add("hidden-content");
				}
			}

			function fetchKhoaFromAPI1() {
				console.log("Fetching data from API...");
				fetch("http://222.255.130.100:7070/vku/admin/api/document/getAll")
					.then(response => response.json())
					.then(data => {
						console.log("Data from API:", data);
						displayKhoa1(data);
					})
					.catch(error => console.error("Error fetching data:", error));
			}

			function displayKhoa1(data) {
				var khoaElement = document.getElementById("khoa1");
				if (data && data.length > 0) {
					var khoaCounts = {};
					data.forEach(item => {
						var khoa = item.khoa.khoa; // Access the 'khoa' property directly
						khoaCounts[khoa] = (khoaCounts[khoa] || 0) + 1;
					});

					var html = "";
					for (var khoa in khoaCounts) {
						html += `<a href="/admin/statistical/khoa" class="dropdown-item notify-item">`;
						html += `${khoa}: ${khoaCounts[khoa]}`;
						html += `</a>`;
					}

					khoaElement.innerHTML = html;
				} else {
					khoaElement.textContent = "Thống kê học phí không có dữ liệu";
				}
			}


			// Fetch the khoa data when the page loads (optional)
			window.onload = function () {
				fetchKhoaFromAPI1();
			};
			
			
			

      