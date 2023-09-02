<script>
  import GetList from '../component/GetList.svelte'
  import TestComponent from "../../../components/TestComponent.svelte";
  import Button from "../../../components/Button.svelte";

  let counter = 0;

  function click() {
    counter++;
  }
  
  function logout() {
    fetch("/logoutUser", {method: "POST"})
      .then(response => response.json())
      .then(response => {
        if(response.status === "ok") {
          location.href = "/login";
        }
      });
  }
</script>

<h1>Home sweet Homes</h1>


<div class="iconHeart">
  <h1>hello icon heart</h1>
</div>


<Button onClick={logout} text="Log Out"/>


<TestComponent></TestComponent>
<GetList/>
