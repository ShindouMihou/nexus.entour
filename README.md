![Splashscreen](https://github.com/ShindouMihou/Nexus/assets/69381903/e2e2118b-07c4-4c49-9322-0507dc1ebf5c)

#

<div align="center"><i>Entour the Reactivity</i></div>
<br/>

Nexus Entour is a set of handy built-in components, hooks and tools that can be used with Nexus.R. It is built to make 
the use of Nexus.R more productive by reducing the amount of code that you need to add different components.

## Overview

In this section, all the components, hooks and other tools that have been added by Nexus Entour is displayed. Clicking on 
one of them should lead you to their relevant example.

- `components`
- `hooks`
  - [`useHideButtons`](examples/HideButtonsExample.kt) is a hook that returns a 
  `React.Writable<Boolean>` that will change after a given amount of time, primarily used to hide components and 
  free up opened  resources, such as unused listeners. It resets the timer every time a re-render happens.
  - [`useConfirmationMenu`](examples/ConfirmationMenuExample.kt) is a hook that creates a simple Confirmation Menu. 
  This hook returns a `ConfirmationMenu` data class which can be destructed into `(confirmationState, ConfirmationMenu)`
  which are `React.Writable<ConfirmationMenuState>` and a `React.Component` respectively.