import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISystemConfiguration, SystemConfiguration } from '../system-configuration.model';

import { SystemConfigurationService } from './system-configuration.service';

describe('SystemConfiguration Service', () => {
  let service: SystemConfigurationService;
  let httpMock: HttpTestingController;
  let elemDefault: ISystemConfiguration;
  let expectedResult: ISystemConfiguration | ISystemConfiguration[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SystemConfigurationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      currentPeriodStartMoment: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          currentPeriodStartMoment: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SystemConfiguration', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          currentPeriodStartMoment: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          currentPeriodStartMoment: currentDate,
        },
        returnedFromService
      );

      service.create(new SystemConfiguration()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SystemConfiguration', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currentPeriodStartMoment: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          currentPeriodStartMoment: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SystemConfiguration', () => {
      const patchObject = Object.assign(
        {
          currentPeriodStartMoment: currentDate.format(DATE_TIME_FORMAT),
        },
        new SystemConfiguration()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          currentPeriodStartMoment: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SystemConfiguration', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          currentPeriodStartMoment: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          currentPeriodStartMoment: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SystemConfiguration', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSystemConfigurationToCollectionIfMissing', () => {
      it('should add a SystemConfiguration to an empty array', () => {
        const systemConfiguration: ISystemConfiguration = { id: 123 };
        expectedResult = service.addSystemConfigurationToCollectionIfMissing([], systemConfiguration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemConfiguration);
      });

      it('should not add a SystemConfiguration to an array that contains it', () => {
        const systemConfiguration: ISystemConfiguration = { id: 123 };
        const systemConfigurationCollection: ISystemConfiguration[] = [
          {
            ...systemConfiguration,
          },
          { id: 456 },
        ];
        expectedResult = service.addSystemConfigurationToCollectionIfMissing(systemConfigurationCollection, systemConfiguration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SystemConfiguration to an array that doesn't contain it", () => {
        const systemConfiguration: ISystemConfiguration = { id: 123 };
        const systemConfigurationCollection: ISystemConfiguration[] = [{ id: 456 }];
        expectedResult = service.addSystemConfigurationToCollectionIfMissing(systemConfigurationCollection, systemConfiguration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemConfiguration);
      });

      it('should add only unique SystemConfiguration to an array', () => {
        const systemConfigurationArray: ISystemConfiguration[] = [{ id: 123 }, { id: 456 }, { id: 86879 }];
        const systemConfigurationCollection: ISystemConfiguration[] = [{ id: 123 }];
        expectedResult = service.addSystemConfigurationToCollectionIfMissing(systemConfigurationCollection, ...systemConfigurationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const systemConfiguration: ISystemConfiguration = { id: 123 };
        const systemConfiguration2: ISystemConfiguration = { id: 456 };
        expectedResult = service.addSystemConfigurationToCollectionIfMissing([], systemConfiguration, systemConfiguration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(systemConfiguration);
        expect(expectedResult).toContain(systemConfiguration2);
      });

      it('should accept null and undefined values', () => {
        const systemConfiguration: ISystemConfiguration = { id: 123 };
        expectedResult = service.addSystemConfigurationToCollectionIfMissing([], null, systemConfiguration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(systemConfiguration);
      });

      it('should return initial array if no SystemConfiguration is added', () => {
        const systemConfigurationCollection: ISystemConfiguration[] = [{ id: 123 }];
        expectedResult = service.addSystemConfigurationToCollectionIfMissing(systemConfigurationCollection, undefined, null);
        expect(expectedResult).toEqual(systemConfigurationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
