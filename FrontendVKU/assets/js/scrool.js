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