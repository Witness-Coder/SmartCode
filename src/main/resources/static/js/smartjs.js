function toggleSidebar() {
		        document.body.classList.toggle('sidebar-open');
				// Hide the hamburger button when sidebar is open
				           if (document.body.classList.contains('sidebar-open')) {
				               document.querySelector('.hamburger').style.display = 'none';
				           } else {
				               document.querySelector('.hamburger').style.display = 'block';
				           }
				       }
					   

				       // Close sidebar if clicked outside
 window.addEventListener('click', function(e) {
				           var sidebar = document.getElementById('sidebar');
				           var hamburger = document.querySelector('.hamburger');
				           if (!sidebar.contains(e.target) && !hamburger.contains(e.target) && document.body.classList.contains('sidebar-open')) {
				               document.body.classList.remove('sidebar-open');
				               document.querySelector('.hamburger').style.display = 'block'; // Show hamburger button
				           }
				   });
				   
				   
				   
				   
				   
				   
				   
				 
 $('#imageModal').on('show.bs.modal', function (event) {
				              var button = $(event.relatedTarget);
				              var imageData = button.data('image');
				              var modal = $(this);
				              modal.find('#modalImage').attr('src', 'data:image/png;base64,' + imageData);
				          });
						  
						  
						  function openImagePopup() {
						      openImagePopup('profilePic', 'imagePopup', 'popupImage');
						  }
						  function closeImagePopup() {
						      closeImagePopup('imagePopup');
						  }
				   	
				   	
						  
						  /*
						    Function hii inaitwa pale user akichagua picha
						  */
function uploadCustomerProfileImage(input) {

						      // Chukua file aliyochagua user
						      const file = input.files[0];

						      // Kama hakuna file, toka
						      if (!file) return;

						      // FormData hutumika kutuma file kwenda backend
						      const formData = new FormData();

						      // "image" lazima ifanane na @RequestParam("image")
						      formData.append("image", file);

						      // Tuma request kwenda Spring Boot Controller
						      fetch("/customer/update-profile-image", {
						          method: "POST",
						          body: formData
						      })
						      .then(res => {
						          if (!res.ok) throw new Error("Upload failed");
						          return res.text();
						      })
						      .then(() => {
						          // Refresh image bila page reload
						          document.getElementById("profilePic").src =
						              "/customer/image/" + Date.now();
						      })
						      .catch(() => alert("Image upload failed"));
						  }
						 
						 
						 

						  // ===== Editing modal =====
						  function openManagerProfileModal() {
						      const sidebarImg = document.getElementById('managerPic');
						      const modalImg = document.getElementById('modalManagerPic');

						      // Copy image source from sidebar
						      modalImg.src = sidebarImg.src;

						      document.getElementById('managerProfileModal').style.display = 'block';
						  }
						  function closeManagerProfileModal() {
						      document.getElementById('managerProfileModal').style.display = 'none';
						  }

						  // ===== Full-size modal =====
						  function openFullSizeModal() {
						      const modalImg = document.getElementById('fullSizeProfilePic');
						      const editModalImg = document.getElementById('modalManagerPic');

						      // Copy the current profile image src from edit modal
						      modalImg.src = editModalImg.src;

						      document.getElementById('fullSizeProfileModal').style.display = 'block';
						  }

						  function closeFullSizeModal() {
						      document.getElementById('fullSizeProfileModal').style.display = 'none';
						  }

						  // ===== Upload profile image (Editing modal) =====
						  function uploadManagerProfileImage(input) {
						      const file = input.files[0];
						      if (!file) return;

						      const reader = new FileReader();
						      reader.onload = function(e) {
						          // Update all image views
						          document.getElementById('modalManagerPic').src = e.target.result;
						          document.getElementById('managerPic').src = e.target.result;
						          document.getElementById('fullSizeProfilePic').src = e.target.result;
						      };
						      reader.readAsDataURL(file);

						      // TODO: Send file to backend to save
						  }

						  // ===== Upload full-size image =====
						  function uploadFullSizeProfileImage(input) {
						      const file = input.files[0];
						      if (!file) return;

						      const reader = new FileReader();
						      reader.onload = function(e) {
						          // Update all image views
						          document.getElementById('fullSizeProfilePic').src = e.target.result;
						          document.getElementById('modalManagerPic').src = e.target.result;
						          document.getElementById('managerPic').src = e.target.result;
						      };
						      reader.readAsDataURL(file);

						      // TODO: Send file to backend to save
						  }

						  // ===== Save edited manager name =====
						  function saveManagerProfile() {
						      const newName = document.getElementById('managerModalName').value;

						      // Update sidebar display
						      const h6 = document.querySelector('h6[th\\:text="${session.loggedInUser}"]');
						      if (h6) h6.textContent = newName;

						      closeManagerProfileModal();

						      // TODO: Send newName to backend to save
						  }
				let oldManagerName = "";

						  function openEditNameModal() {
						      const nameEl = document.getElementById("managerNameDisplay");
						      const input = document.getElementById("editNameInput");

						      oldManagerName = nameEl.textContent.trim();
						      input.value = oldManagerName;

						      const modal = new bootstrap.Modal(
						          document.getElementById("editNameModal")
						      );
						      modal.show();

						      // focus & highlight old name
						      setTimeout(() => {
						          input.focus();
						          input.select();
						      }, 200);
						  }

						  function confirmEditName() {
						      const newName = document.getElementById("editNameInput").value.trim();
						      if (newName === "") return;

						      // update UI instantly
						      document.getElementById("managerNameDisplay").textContent = newName;

						      // TODO: call backend to save name
						      // saveManagerNameToServer(newName);

						      bootstrap.Modal.getInstance(
						          document.getElementById("editNameModal")
						      ).hide();
						  }

						  function cancelEditName() {
						      // restore old name
						      document.getElementById("managerNameDisplay").textContent = oldManagerName;

						      bootstrap.Modal.getInstance(
						          document.getElementById("editNameModal")
						      ).hide();
						  }
