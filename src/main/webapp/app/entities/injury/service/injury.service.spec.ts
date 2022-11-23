import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInjury, Injury } from '../injury.model';

import { InjuryService } from './injury.service';

describe('Injury Service', () => {
  let service: InjuryService;
  let httpMock: HttpTestingController;
  let elemDefault: IInjury;
  let expectedResult: IInjury | IInjury[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InjuryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      moment: currentDate,
      estReturnDate: currentDate,
      reason: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          moment: currentDate.format(DATE_TIME_FORMAT),
          estReturnDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Injury', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          moment: currentDate.format(DATE_TIME_FORMAT),
          estReturnDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          moment: currentDate,
          estReturnDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Injury()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Injury', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          moment: currentDate.format(DATE_TIME_FORMAT),
          estReturnDate: currentDate.format(DATE_FORMAT),
          reason: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          moment: currentDate,
          estReturnDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Injury', () => {
      const patchObject = Object.assign(
        {
          moment: currentDate.format(DATE_TIME_FORMAT),
          estReturnDate: currentDate.format(DATE_FORMAT),
          reason: 'BBBBBB',
        },
        new Injury()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          moment: currentDate,
          estReturnDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Injury', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          moment: currentDate.format(DATE_TIME_FORMAT),
          estReturnDate: currentDate.format(DATE_FORMAT),
          reason: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          moment: currentDate,
          estReturnDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Injury', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInjuryToCollectionIfMissing', () => {
      it('should add a Injury to an empty array', () => {
        const injury: IInjury = { id: 123 };
        expectedResult = service.addInjuryToCollectionIfMissing([], injury);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(injury);
      });

      it('should not add a Injury to an array that contains it', () => {
        const injury: IInjury = { id: 123 };
        const injuryCollection: IInjury[] = [
          {
            ...injury,
          },
          { id: 456 },
        ];
        expectedResult = service.addInjuryToCollectionIfMissing(injuryCollection, injury);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Injury to an array that doesn't contain it", () => {
        const injury: IInjury = { id: 123 };
        const injuryCollection: IInjury[] = [{ id: 456 }];
        expectedResult = service.addInjuryToCollectionIfMissing(injuryCollection, injury);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(injury);
      });

      it('should add only unique Injury to an array', () => {
        const injuryArray: IInjury[] = [{ id: 123 }, { id: 456 }, { id: 60774 }];
        const injuryCollection: IInjury[] = [{ id: 123 }];
        expectedResult = service.addInjuryToCollectionIfMissing(injuryCollection, ...injuryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const injury: IInjury = { id: 123 };
        const injury2: IInjury = { id: 456 };
        expectedResult = service.addInjuryToCollectionIfMissing([], injury, injury2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(injury);
        expect(expectedResult).toContain(injury2);
      });

      it('should accept null and undefined values', () => {
        const injury: IInjury = { id: 123 };
        expectedResult = service.addInjuryToCollectionIfMissing([], null, injury, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(injury);
      });

      it('should return initial array if no Injury is added', () => {
        const injuryCollection: IInjury[] = [{ id: 123 }];
        expectedResult = service.addInjuryToCollectionIfMissing(injuryCollection, undefined, null);
        expect(expectedResult).toEqual(injuryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
