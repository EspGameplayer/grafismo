import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISuspension, Suspension } from '../suspension.model';

import { SuspensionService } from './suspension.service';

describe('Suspension Service', () => {
  let service: SuspensionService;
  let httpMock: HttpTestingController;
  let elemDefault: ISuspension;
  let expectedResult: ISuspension | ISuspension[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SuspensionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      matches: 0,
      moment: currentDate,
      reason: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          moment: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Suspension', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          moment: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          moment: currentDate,
        },
        returnedFromService
      );

      service.create(new Suspension()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Suspension', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          matches: 1,
          moment: currentDate.format(DATE_TIME_FORMAT),
          reason: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          moment: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Suspension', () => {
      const patchObject = Object.assign(
        {
          matches: 1,
          moment: currentDate.format(DATE_TIME_FORMAT),
          reason: 'BBBBBB',
        },
        new Suspension()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          moment: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Suspension', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          matches: 1,
          moment: currentDate.format(DATE_TIME_FORMAT),
          reason: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          moment: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Suspension', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSuspensionToCollectionIfMissing', () => {
      it('should add a Suspension to an empty array', () => {
        const suspension: ISuspension = { id: 123 };
        expectedResult = service.addSuspensionToCollectionIfMissing([], suspension);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(suspension);
      });

      it('should not add a Suspension to an array that contains it', () => {
        const suspension: ISuspension = { id: 123 };
        const suspensionCollection: ISuspension[] = [
          {
            ...suspension,
          },
          { id: 456 },
        ];
        expectedResult = service.addSuspensionToCollectionIfMissing(suspensionCollection, suspension);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Suspension to an array that doesn't contain it", () => {
        const suspension: ISuspension = { id: 123 };
        const suspensionCollection: ISuspension[] = [{ id: 456 }];
        expectedResult = service.addSuspensionToCollectionIfMissing(suspensionCollection, suspension);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(suspension);
      });

      it('should add only unique Suspension to an array', () => {
        const suspensionArray: ISuspension[] = [{ id: 123 }, { id: 456 }, { id: 5715 }];
        const suspensionCollection: ISuspension[] = [{ id: 123 }];
        expectedResult = service.addSuspensionToCollectionIfMissing(suspensionCollection, ...suspensionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const suspension: ISuspension = { id: 123 };
        const suspension2: ISuspension = { id: 456 };
        expectedResult = service.addSuspensionToCollectionIfMissing([], suspension, suspension2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(suspension);
        expect(expectedResult).toContain(suspension2);
      });

      it('should accept null and undefined values', () => {
        const suspension: ISuspension = { id: 123 };
        expectedResult = service.addSuspensionToCollectionIfMissing([], null, suspension, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(suspension);
      });

      it('should return initial array if no Suspension is added', () => {
        const suspensionCollection: ISuspension[] = [{ id: 123 }];
        expectedResult = service.addSuspensionToCollectionIfMissing(suspensionCollection, undefined, null);
        expect(expectedResult).toEqual(suspensionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
