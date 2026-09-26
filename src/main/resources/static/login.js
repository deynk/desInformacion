const loginForm = document.getElementById("loginForm");
const formStatus = document.getElementById("formStatus");


function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function setStatus(message, type) {
  formStatus.textContent = message;
  formStatus.className = `form-status ${type}`;
}

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  const formData = new FormData(loginForm);
  const email = String(formData.get("email") || "").trim();
  const password = String(formData.get("password") || "").trim();
  const emailRegex = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/i;

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


  const submitButton = loginForm.querySelector("button[type='submit']");
  submitButton.disabled = true;
  setStatus("Leyendo información...", "pending");

  const controller = new AbortController();

  const timeout = setTimeout(() => {
    controller.abort();
  }, 5000); // 5 segundos

  try {
    const response = await fetch("/api/users/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ email, password }),
      signal: controller.signal
    });

    clearTimeout(timeout);

    switch (true) {
      case response.ok:
        setStatus("Usuario existente.", "success");
        break;

      case response.status === 401:
        setStatus("Email o contraseña incorrectos", "error");
        break;

      default:
        setStatus(
            "Vaya, no hemos podido conectarnos a los servidores. Vuelve a intentarlo más tarde :(",
            "error"
        );
        break;
    }

  } catch (error) {

    if (error.name === "AbortError") {
      setStatus(
          "El servidor está tardando demasiado en responder. Vuelve a intentarlo más tarde.",
          "error"
      );
    } else {
      setStatus(
          "Vaya, no hemos podido conectarnos a los servidores. Vuelve a intentarlo más tarde :(",
          "error"
      );
    }
  }
  finally {
    submitButton.disabled = false;
  }


});
