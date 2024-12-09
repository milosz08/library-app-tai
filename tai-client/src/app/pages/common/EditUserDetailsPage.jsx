import { useEffect, useState } from 'react';
import { Box, Button, Container, Typography } from '@mui/material';
import {
  details,
  updateAddressDetails,
  updatePersonalDetails,
} from '../../../api/userApi';
import FormTextField from '../../../components/common/FormTextField';
import { useAlert } from '../../../hooks/useAlert';
import { useAuth } from '../../../hooks/useAuth';
import { useLoader } from '../../../hooks/useLoader';

const EditUserDetailsPage = () => {
  const [personalDetails, setPersonalDetails] = useState({
    firstName: '',
    lastName: '',
  });
  const [addressDetails, setAddressDetails] = useState({
    city: '',
    street: '',
    buildingNumber: '',
    apartmentNumber: '',
  });
  const { setIsLoading } = useLoader();
  const { addAlert } = useAlert();
  const { role } = useAuth();

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        setIsLoading(true);
        const data = await details();
        setPersonalDetails({
          firstName: data.firstName || '',
          lastName: data.lastName || '',
        });
        setAddressDetails({
          city: data.address.city || '',
          street: data.address.street || '',
          buildingNumber: data.address.buildingNumber || '',
          apartmentNumber: data.address.apartmentNumber || '',
        });
      } catch (error) {
        addAlert('Nie udało się załadować danych użytkownika.', 'error');
      } finally {
        setIsLoading(false);
      }
    };

    fetchDetails();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handlePersonalDetailsChange = e => {
    setPersonalDetails({ ...personalDetails, [e.target.name]: e.target.value });
  };

  const handleAddressDetailsChange = e => {
    setAddressDetails({ ...addressDetails, [e.target.name]: e.target.value });
  };

  const handlePersonalDetailsSubmit = async e => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await updatePersonalDetails(personalDetails);
      addAlert('Dane osobowe zostały zaktualizowane.', 'success');
    } catch (error) {
      addAlert('Wystąpił błąd podczas aktualizacji danych osobowych.', 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleAddressDetailsSubmit = async e => {
    e.preventDefault();
    setIsLoading(true);
    try {
      await updateAddressDetails(addressDetails);
      addAlert('Adres zamieszkania został zaktualizowany.', 'success');
    } catch (error) {
      addAlert('Wystąpił błąd podczas aktualizacji adresu.', 'error');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container
      maxWidth="md"
      sx={{
        bgcolor: 'custom.900',
        padding: 4,
        borderRadius: 2,
        mt: 5,
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
      }}>
      <Typography variant="h5" sx={{ color: 'white', mb: 3 }}>
        Edycja danych użytkownika
      </Typography>

      <Box
        key="personalDetails"
        component="form"
        onSubmit={handlePersonalDetailsSubmit}
        sx={{ mb: 5 }}>
        <Typography variant="h6" sx={{ color: 'white', mb: 2 }}>
          Dane osobowe
        </Typography>
        <Box
          display="flex"
          flexWrap="wrap"
          gap={2}
          sx={{
            '@media (max-width:900px)': {
              flexDirection: 'column',
            },
          }}>
          <Box
            key="firstName"
            sx={{
              flexBasis: '49%',
              width: '100%',
              '@media (max-width:900px)': {
                flexBasis: '100%',
              },
            }}>
            <FormTextField
              label="Imię"
              name="firstName"
              value={personalDetails.firstName}
              onChange={handlePersonalDetailsChange}
            />
          </Box>
          <Box
            key="lastName"
            flexBasis="49%"
            sx={{ flexBasis: { xs: '100%', sm: '49%' } }}>
            <FormTextField
              label="Nazwisko"
              name="lastName"
              value={personalDetails.lastName}
              onChange={handlePersonalDetailsChange}
            />
          </Box>
        </Box>
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{
            mt: 3,
            bgcolor: 'customBlue.600',
            color: 'white',
            '&:hover': { bgcolor: 'customBlue.500' },
          }}>
          Zaktualizuj dane osobowe
        </Button>
      </Box>

      {role === 'CUSTOMER' && (
        <Box component="form" onSubmit={handleAddressDetailsSubmit}>
          <Typography variant="h6" sx={{ color: 'white', mb: 2 }}>
            Adres zamieszkania
          </Typography>
          <Box
            display="flex"
            flexWrap="wrap"
            gap={2}
            sx={{
              '@media (max-width:900px)': {
                flexDirection: 'column',
              },
            }}>
            <Box
              sx={{
                flexBasis: '49%',
                width: '100%',
                '@media (max-width:900px)': {
                  flexBasis: '100%',
                },
              }}>
              <FormTextField
                label="Miejscowość"
                name="city"
                value={addressDetails.city}
                onChange={handleAddressDetailsChange}
              />
            </Box>
            <Box flexBasis="49%" sx={{ flexBasis: { xs: '100%', sm: '49%' } }}>
              <FormTextField
                label="Ulica"
                name="street"
                value={addressDetails.street}
                onChange={handleAddressDetailsChange}
              />
            </Box>
            <Box flexBasis="49%" sx={{ flexBasis: { xs: '100%', sm: '49%' } }}>
              <FormTextField
                label="Numer budynku"
                name="buildingNumber"
                value={addressDetails.buildingNumber}
                onChange={handleAddressDetailsChange}
              />
            </Box>
            <Box flexBasis="49%" sx={{ flexBasis: { xs: '100%', sm: '49%' } }}>
              <FormTextField
                label="Numer apartamentu (opcjonalne)"
                name="apartmentNumber"
                value={addressDetails.apartmentNumber}
                onChange={handleAddressDetailsChange}
              />
            </Box>
          </Box>
          <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{
              mt: 3,
              bgcolor: 'customBlue.600',
              color: 'white',
              '&:hover': { bgcolor: 'customBlue.500' },
            }}>
            Zaktualizuj adres
          </Button>
        </Box>
      )}
    </Container>
  );
};

export default EditUserDetailsPage;
