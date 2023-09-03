 let isKhoaDropdownOpen = false;

    function toggleKhoaDropdown1(event) {
        // Prevent the click event from bubbling up to parent elements
        event.stopPropagation();

        const khoaContainer = document.getElementById("khoacontainer1");
        const dropdownMenu = document.querySelector(".dropdown-menu");

        if (isKhoaDropdownOpen) {
            khoaContainer.classList.add("hidden-content");
            dropdownMenu.classList.remove("show");
        } else {
            khoaContainer.classList.remove("hidden-content");
            dropdownMenu.classList.add("show");
        }

        isKhoaDropdownOpen = !isKhoaDropdownOpen;
    }

    // Close the dropdown if the user clicks outside of it
    window.addEventListener("click", function(event) {
        if (!event.target.matches(".menu-title")) {
            const dropdownMenu = document.querySelector(".dropdown-menu");
            dropdownMenu.classList.remove("show");
            isKhoaDropdownOpen = false;
        }
    });
    
    
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
			
			
			
