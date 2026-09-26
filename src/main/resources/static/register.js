const registerForm = document.getElementById("registerForm");
const formStatus = document.getElementById("formStatus");


// Password fields
const passwordField = document.getElementById("password");
const repPasswordField = document.getElementById("repPassword");









function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function setStatus(message, type) {
  formStatus.textContent = message;
  formStatus.className = `form-status ${type}`;
}
function clearStatus(){
  formStatus.textContent = "";
  formStatus.className = `form-status `;
}

passwordField.addEventListener("input", ()=>{
  if(passwordField.value !== repPasswordField.value)
    setStatus("Las contraseñas no coinciden", "error");
  else
    clearStatus();
})
repPasswordField.addEventListener("input", ()=>{
  if(passwordField.value !== repPasswordField.value)
    setStatus("Las contraseñas no coinciden", "error");
  else
    clearStatus();
})

registerForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  const formData = new FormData(registerForm);
  const name = String(formData.get("name") || "").trim();
  const email = String(formData.get("email") || "").trim();
  const password = String(formData.get("password") || "");
  const repPassword = String(formData.get("repPassword") || "");
  const emailRegex = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/i;
  /* Sí, eso es el regex para el email*/

  if (!name) {
    setStatus("Escribe un nombre para continuar.", "error");
    return;
  }
  if (name.length > 50) {
    setStatus("El nombre es demasiado largo.", "error");
    return;
  }


  if (!email) {
    setStatus("Escribe un email para continuar.", "error");
    return;
  }
  if(email.length > 320){
    setStatus("El email no puede superar los 320 caracteres.", "error");
    return;
  }
  if (!emailRegex.test(email)) {
    setStatus("El email no es válido.", "error");
    return;
  }


  if (!password) {
    setStatus("Escribe una contraseña para continuar.", "error");
    return;
  }
  if (password.length < 8) {
    setStatus("La contraseña debe tener al menos 8 caracteres.", "error");
    return;
  }
  if (password.length > 20) {
    setStatus("La contraseña debe tener como máximo 20 caracteres.", "error");
    return;
  }

  if(passwordField.value !== repPasswordField.value){
    setStatus("Las contraseñas no coinciden", "error");
    return;
  }



  const submitButton = registerForm.querySelector("button[type='submit']");
  submitButton.disabled = true;
  setStatus("Creando usuario...", "pending");

  try {
    const response = await fetch("/api/users/", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email, password })
    });

    if (!response.ok) {
      const errorCode = response.status
      if(errorCode === 409) setStatus("Email ya en uso, introduzca otro email", "error")
      else if(errorCode === 400) setStatus("Introduzca datos válidos", "error")
      else setStatus("Ha ocurrido un error al registrar el usuario.", "error");
      submitButton.disabled = false;
    }
    else{
      //registerForm.reset();
      setStatus(`Usuario creado correctamente. En 5 segundos te redirigiremos a la página principal.`, "success");
      //await sleep(5000);
      //window.location.href = "/index.html";
    }

  } catch (error) {
    setStatus("No se pudo establecer conexión con los servidores", "error")
  } finally {
    submitButton.disabled = false;
  }
});
