import {
  createSystem,
  defaultConfig,
  defineConfig,
  defineRecipe,
  defineSlotRecipe
} from '@chakra-ui/react'
import { fieldAnatomy, nativeSelectAnatomy } from '@chakra-ui/react/anatomy'

const buttonRecipe = defineRecipe({
  base: {
    fontWeight: 'bold',
    borderRadius: 'full'
  },
  variants: {
    variant: {
      primary: {
        color: 'white',
        bg: 'primary',
        _hover: {
          bg: 'primaryHover'
        },
        transitionDuration: '0.4s'
      },
      secondary: {
        color: 'red',
        bg: '#635FC7',
        _hover: {
          bg: 'primaryHover'
        },
        transitionDuration: '0.4s'
      },

      destructive: {
        color: 'white',
        bg: 'red',
        _hover: {
          bg: 'redHover'
        },
        transitionDuration: '0.4s'
      }
    },
    sizes: {
      lg: {
        fontSize: 'lg',
        px: '12',
        py: '6'
      },
      sm: {
        fontSize: 'sm',
        px: '4',
        py: '2'
      },
      xs: {
        fontSize: 'xs',
        px: '2',
        py: '2'
      }
    }
  },
  defaultVariants: {
    variant: 'primary',
    sizes: 'lg'
  }
})

const inputRecipe = defineRecipe({
  base: {
    px: 3,
    py: 6,
    textStyle: 'lg',
    color: 'white',
    '--input-height': 'sizes.16',
    '--focus-color': 'white',
    '--error-color': 'red',
    _focus: {},
    _placeholder: {
      color: 'gray/25'
    }
  }
})

const fieldSlotRecipe = defineSlotRecipe({
  slots: fieldAnatomy.keys(),
  base: {
    requiredIndicator: {
      color: 'red',
      lineHeight: '1'
    },
    root: {
      display: 'flex',
      width: '100%',
      position: 'relative',
      gap: '1.5'
    },
    label: {
      display: 'flex',
      alignItems: 'center',
      textAlign: 'start',
      textStyle: 'md',
      color: 'white',
      fontWeight: 'bold',
      fontSize: 'md',
      gap: '1',
      userSelect: 'none',
      _disabled: {
        opacity: '0.5'
      }
    },
    errorText: {
      display: 'inline-flex',
      alignItems: 'center',
      fontWeight: 'medium',
      gap: '1',
      color: 'red',
      textStyle: 'xs'
    },
    helperText: {
      color: 'primary',
      textStyle: 'xs'
    }
  }
})

const nativeSelectSlotRecipe = defineSlotRecipe({
  slots: nativeSelectAnatomy.keys(),
  base: {
    root: {
      height: 'fit-content',
      display: 'flex',
      width: '100%',
      position: 'relative'
    },
    field: {
      width: '100%',
      minWidth: '0',
      outline: '0',
      appearance: 'none',
      borderRadius: 'l2',
      '--error-color': 'red',
      color: 'white',
      _disabled: {
        layerStyle: 'disabled'
      },
      _invalid: {
        focusRingColor: 'var(--error-color)',
        borderColor: 'var(--error-color)'
      },
      focusVisibleRing: 'inside',
      lineHeight: 'normal',
      '& > option, & > optgroup': {
        bg: 'bg'
      }
    },
    indicator: {
      position: 'absolute',
      display: 'inline-flex',
      alignItems: 'center',
      justifyContent: 'center',
      pointerEvents: 'none',
      top: '50%',
      transform: 'translateY(-50%)',
      height: '100%',
      color: 'primary',
      _disabled: {
        opacity: '0.5'
      },
      _invalid: {
        color: 'error'
      },
      _icon: {
        width: '1em',
        height: '1em'
      }
    }
  }
})

const config = defineConfig({
  theme: {
    tokens: {
      colors: {
        primary: { value: '#635FC7' },
        primaryHover: { value: '#A8A4FF' },
        black: { value: '#000112' },
        dark: { value: '#20212C' },
        darkAlt: { value: '#2B2C37' },
        darkHover: { value: '#3E3F4E' },
        gray: { value: '#828FA3' },
        lightGray: { value: '#E4EBFA' },
        lightBg: { value: '#F4F7FD' },
        white: { value: '#FFFFFF' },
        red: { value: '#EA5555' },
        redHover: { value: '#FF9898' }
      },
      fonts: {
        jakarta: { value: "'Plus Jakarta Sans', sans-serif" }
      },
      fontSizes: {
        xl: { value: '24px' },
        lg: { value: '18px' },
        md: { value: '15px' },
        sm: { value: '12px' }
      },
      lineHeights: {
        xl: { value: '30px' },
        lg: { value: '23px' },
        md: { value: '19px' },
        sm: { value: '15px' }
      }
    },
    recipes: {
      button: buttonRecipe,
      input: inputRecipe
    },
    slotRecipes: {
      field: fieldSlotRecipe,
      nativeSelect: nativeSelectSlotRecipe
    }
  },
  globalCss: {
    'html, body': {
      bg: 'dark',
      color: 'gray',
      minH: '100vh',
      fontFamily: 'jakarta',
      fontSize: 'initial',
      colorScheme: 'dark'
    }
  }
})

export const system = createSystem(defaultConfig, config)
